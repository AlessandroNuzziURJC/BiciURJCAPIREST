package group12.biciurjc.service;

import group12.biciurjc.model.User;
import group12.biciurjc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public void save(User user) {
        repository.save(user);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public Optional<User> findById(long id) {
        return repository.findById(id);
    }
}