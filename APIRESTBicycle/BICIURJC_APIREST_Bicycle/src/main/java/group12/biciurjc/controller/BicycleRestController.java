package group12.biciurjc.controller;

import group12.biciurjc.service.BicycleService;
import group12.biciurjc.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BicycleRestController {

    @Autowired
    private BicycleService bicycleService;

    @Autowired
    private StationService stationService;
}