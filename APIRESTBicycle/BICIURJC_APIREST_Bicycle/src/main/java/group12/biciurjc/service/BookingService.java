package group12.biciurjc.service;

import group12.biciurjc.model.Booking;
import group12.biciurjc.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository repository;

    public List<Booking> findAll() {
        return repository.findAll();
    }

    public void save(Booking booking) {
        repository.save(booking);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Optional<Booking> findById(long id) {
        return repository.findById(id);
    }
}