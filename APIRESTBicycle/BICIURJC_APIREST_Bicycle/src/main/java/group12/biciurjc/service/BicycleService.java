package group12.biciurjc.service;

import group12.biciurjc.model.Bicycle;
import group12.biciurjc.model.Station;
import group12.biciurjc.model.Status;
import group12.biciurjc.repository.BicycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static group12.biciurjc.model.Status.WITHOUT_BASE;

@Service
public class BicycleService {

    @Autowired
    private BicycleRepository repository;

    public List<Bicycle> findAll() {
        return repository.findAll();
    }

    public void save(Bicycle bicycle) {
        repository.save(bicycle);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public void remove(Bicycle bicycle) {
        if (!bicycle.getStatus().equals(Status.WITHDRAWAL)) {
            Station station = bicycle.getStation();

            if (station != null) {
                station.deleteBicycle(bicycle);
            }

            bicycle.setStatus(Status.WITHDRAWAL);
            this.save(bicycle);
        }
    }

    public Optional<Bicycle> findById(long id) {
        return repository.findById(id);
    }
}