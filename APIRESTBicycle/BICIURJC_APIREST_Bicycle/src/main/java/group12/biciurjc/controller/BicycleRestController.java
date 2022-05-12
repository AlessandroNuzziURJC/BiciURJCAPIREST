package group12.biciurjc.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import group12.biciurjc.model.Bicycle;
import group12.biciurjc.model.Booking;
import group12.biciurjc.model.DTO.BookingDTO;
import group12.biciurjc.model.Station;
import group12.biciurjc.model.Status;
import group12.biciurjc.service.BicycleService;
import group12.biciurjc.service.BookingService;
import group12.biciurjc.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
public class BicycleRestController {

    @Autowired
    private BicycleService bicycleService;

    @Autowired
    private StationService stationService;

    @Autowired
    private BookingService bookingService;

    @Operation(summary = "Crea una nueva reserva para un usuario de una bicicleta asociada a una estación")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "La reserva se ha realizado con éxito",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=BookingDTO.class)
            )}
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Estación, bicicleta o usuario no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "La estación no está activa, la bicicleta no está en estado 'EN-BASE' o no está en dicha estación, o el usuario no tiene suficiente dinero",
            content = @Content
        )
    })
    @PostMapping("api/reservas/")
    private ResponseEntity<BookingDTO> bookBicycle(@Parameter(description = "id de la estación en la que se encuentra la bicicleta") @RequestParam long stationId,
                                                   @Parameter(description = "id de la bicicleta que se va a reservar") @RequestParam long bicycleId,
                                                   @Parameter(description = "id del usuario que quiere reservar la bicicleta") @RequestParam long userId) {

        Optional<Station> optionalStation = stationService.findById(stationId);
        Optional<Bicycle> optionalBicycle = bicycleService.findById(bicycleId);

        if (optionalStation.isPresent() && optionalBicycle.isPresent()) {
            Station station = optionalStation.get();
            Bicycle bicycle = optionalBicycle.get();

            boolean bikeIsInBase = bicycle.getStatus() == Status.IN_BASE;
            boolean bikeIsInThisStation = bicycle.getStation().getId() == station.getId();

            if (station.isActive() && bikeIsInBase && bikeIsInThisStation) {
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8081/api/users/" + userId + "/money";

                try {
                    //At the moment, the price for booking a bike is fixed, 5€. Therefore, the deposit is 10€. Total = 15€
                    ObjectNode data = restTemplate.patchForObject(url, 15, ObjectNode.class);
                    String userName = data.get("name").asText();

                    station.deleteBicycle(bicycle);
                    bicycle.setStatus(Status.RESERVED);
                    Booking booking = new Booking(station, bicycle, userId, userName, 5);
                    bookingService.save(booking);

                    BookingDTO bookingDTO = new BookingDTO(booking.getId(), stationId, bicycleId, userId, userName, booking.getDate(), booking.getPrice());

                    return new ResponseEntity<>(bookingDTO, HttpStatus.CREATED);
                } catch (HttpStatusCodeException exception) {
                    HttpStatus status = exception.getStatusCode();

                    if (status == HttpStatus.NOT_FOUND) {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    } else {
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }
                }
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}