package group12.biciurjc.service;

import group12.biciurjc.model.User;
import group12.biciurjc.repository.UserRepository;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException {
        //Users
        User user1 = new User("Juan Carlos Moreno García", passwordEncoder.encode("pass"));
        User user2 = new User("Alessandro Nuzzi Herrero", passwordEncoder.encode("pass"));
        User user3 = new User("Jhostin David Ortiz Moreno", passwordEncoder.encode("pass"));
        User user4 = new User("Néstor Granado Pérez", passwordEncoder.encode("pass"));
        User user5 = new User("Nuria Mateos Bravo", passwordEncoder.encode("pass"));

        setProfileImage(user1, "/static/images/JuanCarlos.jpg");
        setProfileImage(user2, "/static/images/Alessandro.jpg");
        setProfileImage(user3, "/static/images/Jhostin.jpg");
        setProfileImage(user4, "/static/images/Nestor.jpg");
        setProfileImage(user5, "/static/images/Sara.jpg");

        user1.setBalance(7000.00);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        User userApi1 = new User("Néstor Granado Pérez", passwordEncoder.encode("pass"), 12.55);
        setProfileImage(userApi1, "/static/images/Nestor.jpg");
        userRepository.save(userApi1);
    }

    private void setProfileImage(User user, String classpathResource) throws IOException {
        Resource image = new ClassPathResource(classpathResource);
        user.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
    }
}