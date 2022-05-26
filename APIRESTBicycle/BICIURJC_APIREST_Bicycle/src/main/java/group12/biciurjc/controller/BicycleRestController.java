package group12.biciurjc.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import group12.biciurjc.model.*;
import group12.biciurjc.model.DTO.BookingDTO;
import group12.biciurjc.model.DTO.ReturnDTO;
import group12.biciurjc.model.DTO.BalanceDTO;
import group12.biciurjc.service.BicycleService;
import group12.biciurjc.service.BookingService;
import group12.biciurjc.service.ReturnService;
import group12.biciurjc.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static group12.biciurjc.model.Status.IN_BASE;
import static group12.biciurjc.model.Status.RESERVED;

@RestController
@RequestMapping("/api")
public class BicycleRestController {

    @Autowired
    private BicycleService bicycleService;

    @Autowired
    private StationService stationService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReturnService returnService;

    private static RestTemplate restTemplate = new RestTemplate();
    private static final String URL_USERS_APIREST = "http://localhost:8081/api/users/";
    private static final int PAYMENT = 5;
    private static final int DEPOSIT = PAYMENT * 2;

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
    @PostMapping("/bookings/")
    public ResponseEntity<BookingDTO> bookBicycle(@Parameter(description = "id de la estación en la que se encuentra la bicicleta") @RequestParam long stationId,
                                                   @Parameter(description = "id de la bicicleta que se va a reservar") @RequestParam long bicycleId,
                                                   @Parameter(description = "id del usuario que quiere reservar la bicicleta") @RequestParam long userId) {

        Optional<Station> optionalStation = stationService.findById(stationId);
        Optional<Bicycle> optionalBicycle = bicycleService.findById(bicycleId);

        if (optionalStation.isPresent() && optionalBicycle.isPresent()) {
            Station station = optionalStation.get();
            Bicycle bicycle = optionalBicycle.get();

            boolean bikeIsInBase = bicycle.getStatus() == IN_BASE;
            boolean bikeIsInThisStation = bicycle.getStation().getId() == station.getId();

            if (station.isActive() && bikeIsInBase && bikeIsInThisStation) {

                String url = URL_USERS_APIREST + userId + "/balance";

                try {
                    //At the moment, the price for booking a bike is fixed, 5€. Therefore, the deposit is 10€. Total = 15€
                    ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<BalanceDTO>(new BalanceDTO(-(PAYMENT + DEPOSIT))), ObjectNode.class);

                    if (!responseEntity.getStatusCode().is2xxSuccessful())
                        throw new HttpClientErrorException(responseEntity.getStatusCode());

                    ObjectNode data = responseEntity.getBody();
                    String userName = data.get("name").asText();

                    station.deleteBicycle(bicycle);
                    bicycle.setStatus(RESERVED);
                    stationService.save(station);
                    bicycleService.save(bicycle);

                    Booking booking = new Booking(station, bicycle, userId, userName, PAYMENT);
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

    @Operation(summary = "Crea una nueva devolución de una bicicleta por parte de un usuario a una estación")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "La devolución se ha realizado con éxito",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=ReturnDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estación, bicicleta o usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "La estación no está activa, la bicicleta no está en estado 'RSERVADO', o no caben más bicicletas en la estación",
                    content = @Content
            )
    })
    @PostMapping("/returns/")
    public ResponseEntity<ReturnDTO> returnBicycle(@Parameter(description = "id de la estación en la que se desea dejar la bicicleta") @RequestParam long stationId,
                                                   @Parameter(description = "id de la bicicleta reservada") @RequestParam long bicycleId,
                                                   @Parameter(description = "id del usuario que quiere devolver la bicicleta") @RequestParam long userId) {

        Optional<Station> opStation = stationService.findById(stationId);
        Optional<Bicycle> opBicycle = bicycleService.findById(bicycleId);

        if (opStation.isPresent() && opBicycle.isPresent()) {
            Station station = opStation.get();
            Bicycle bicycle = opBicycle.get();

            boolean conditions = station.isActive() & bicycle.getStatus().equals(RESERVED)
                    & (station.getBicycleCapacity() - station.getBicycles().size()) > 0;

            if (conditions){
                String url = URL_USERS_APIREST + userId + "/balance";

                try {
                    ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<BalanceDTO>(new BalanceDTO(DEPOSIT)), ObjectNode.class);

                    if (!responseEntity.getStatusCode().is2xxSuccessful())
                        throw new HttpClientErrorException(responseEntity.getStatusCode());

                    ObjectNode data = responseEntity.getBody();
                    String userName = data.get("name").asText();

                    station.addBicycle(bicycle);
                    bicycleService.save(bicycle);
                    Return ret = new Return(station, bicycle, userId, userName);
                    returnService.save(ret);

                    ReturnDTO returnDTO = new ReturnDTO(ret.getId(), stationId, bicycleId, userId, userName, ret.getDate());

                    return new ResponseEntity<>(returnDTO, HttpStatus.CREATED);
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