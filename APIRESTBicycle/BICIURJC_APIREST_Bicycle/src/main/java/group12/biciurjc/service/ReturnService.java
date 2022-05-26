package group12.biciurjc.service;

import group12.biciurjc.model.Return;
import group12.biciurjc.repository.ReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnService {

    @Autowired
    private ReturnRepository repository;

    public List<Return> findAll() {
        return repository.findAll();
    }

    public void save(Return ret) {
        repository.save(ret);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Optional<Return> findById(long id) {
        return repository.findById(id);
    }
}