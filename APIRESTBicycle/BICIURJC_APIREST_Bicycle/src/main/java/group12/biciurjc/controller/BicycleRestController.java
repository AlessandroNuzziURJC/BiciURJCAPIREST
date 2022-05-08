package group12.biciurjc.controller;

import group12.biciurjc.model.Bicycle;
import group12.biciurjc.model.Booking;
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
                            schema = @Schema(implementation=Booking.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Station, bicycle or user not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "La estación no está activa, la bicicleta no está en estado 'EN-BASE' o no está en dicha estación, o el usuario no tiene suficiente dinero",
                    content = @Content
            )
    })
    @PostMapping("api/reservas/")
    private ResponseEntity<Booking> bookBicycle(@Parameter(description = "id of the station in which the bicycle is placed") @RequestParam long stationId,
                                                @Parameter(description = "id of the bicyle to be booked") @RequestParam long bicycleId,
                                                @Parameter(description = "id of the user who wants to book the bicycle") @RequestParam long userId) {

        Optional<Station> optionalStation = stationService.findById(stationId);
        Optional<Bicycle> optionalBicycle = bicycleService.findById(bicycleId);

        if (optionalStation.isPresent() && optionalBicycle.isPresent()) {
            Station station = optionalStation.get();
            Bicycle bicycle = optionalBicycle.get();

            boolean bikeIsInBase = bicycle.getStatus() == Status.IN_BASE;
            boolean bikeIsInThisStation = bicycle.getStation().getId() == station.getId();

            if (station.isActive() && bikeIsInBase && bikeIsInThisStation) {
                //Llamada api rest de users

                if (true) {
                    station.deleteBicycle(bicycle);
                    bicycle.setStatus(Status.RESERVED);
                    Booking booking = new Booking(station, bicycle, userId);
                    bookingService.save(booking);

                    return new ResponseEntity<>(booking, HttpStatus.CREATED);
                } else if (true) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}