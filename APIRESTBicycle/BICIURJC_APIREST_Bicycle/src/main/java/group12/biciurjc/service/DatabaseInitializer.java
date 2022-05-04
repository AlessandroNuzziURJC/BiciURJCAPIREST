package group12.biciurjc.service;

import group12.biciurjc.model.Bicycle;
import group12.biciurjc.model.Station;
import group12.biciurjc.repository.BicycleRepository;
import group12.biciurjc.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class DatabaseInitializer {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private BicycleRepository bicycleRepository;

    @PostConstruct
    public void init() {
        //Stations
        Station station1 = new Station("ABC1234567891234", 40.33542999652435, -3.8738490114249413, 5);
        Station station2 = new Station("CBA9876543219876", 40.3504767687328, -3.844611979120157, 10);

        stationRepository.save(station1);
        stationRepository.save(station2);

        //Bicycles
        Bicycle bicycle1 = new Bicycle("AAA1111111111111", "Urbana");
        Bicycle bicycle2 = new Bicycle("BBB2222222222222", "Urbana");
        Bicycle bicycle3 = new Bicycle("CCC3333333333333", "Urbana");
        Bicycle bicycle4 = new Bicycle("DDD4444444444444", "Híbrida");
        Bicycle bicycle5 = new Bicycle("EEE5555555555555", "Urbana");
        Bicycle bicycle6 = new Bicycle("FFF6666666666666", "Urbana");
        Bicycle bicycle7 = new Bicycle("GGG7777777777777", "Híbrida");
        Bicycle bicycle8 = new Bicycle("HHH8888888888888", "Urbana");
        Bicycle bicycle9 = new Bicycle("III9999999999999", "Urbana");

        station1.addBicycle(bicycle1);
        station1.addBicycle(bicycle2);
        station1.addBicycle(bicycle3);
        station1.addBicycle(bicycle4);

        station2.addBicycle(bicycle5);
        station2.addBicycle(bicycle6);
        station2.addBicycle(bicycle7);

        bicycleRepository.save(bicycle1);
        bicycleRepository.save(bicycle2);
        bicycleRepository.save(bicycle3);
        bicycleRepository.save(bicycle4);
        bicycleRepository.save(bicycle5);
        bicycleRepository.save(bicycle6);
        bicycleRepository.save(bicycle7);
        bicycleRepository.save(bicycle8);
        bicycleRepository.save(bicycle9);
    }
}