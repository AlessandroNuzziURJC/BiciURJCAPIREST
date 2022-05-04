package group12.biciurjc.service;

import group12.biciurjc.model.Bicycle;
import group12.biciurjc.model.Station;
import group12.biciurjc.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    @Autowired
    private StationRepository repository;

    public List<Station> findAll() {
        return repository.findAll();
    }

    public void save(Station station) {
        repository.save(station);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public void remove(Station station) {
        List<Bicycle> bicyclesAux = new ArrayList<>();

        for (Bicycle bicycle: station.getBicycles()) {
            bicyclesAux.add(bicycle);
        }

        for (Bicycle bicycle : bicyclesAux) {
            station.deleteBicycle(bicycle);
        }

        station.setActive(false);
    }

    public Optional<Station> findById(long id) {
        return repository.findById(id);
    }
}