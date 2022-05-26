package group12.biciurjc.controller;

import group12.biciurjc.model.DTO.UserDTO;
import group12.biciurjc.model.DTO.UserPostDTO;
import group12.biciurjc.model.DTO.UserPutDTO;
import group12.biciurjc.model.User;
import group12.biciurjc.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public List<UserDTO> getUsers() {
        List<UserDTO> usersDTO = new ArrayList<>();

        for (User user : userService.findAll()) {
            usersDTO.add(new UserDTO(user.getId(), user.getLogin(), user.getName(), user.getDate(), user.isActive(), user.getBalance()));
        }

        return usersDTO;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO(user.getId(), user.getLogin(), user.getName(), user.getDate(), user.isActive(), user.getBalance());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        User user = userService.findById(id).orElseThrow();

        if (user.getImageFile() != null) {
            Resource file = new InputStreamResource(user.getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(user.getImageFile().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody UserPostDTO user) {
        User newUser = new User(user.getLogin(), user.getName(), passwordEncoder.encode(user.getPassword()));
        userService.save(newUser);

        UserDTO userDTO = new UserDTO(newUser.getId(), newUser.getLogin(), newUser.getName(), newUser.getDate(), newUser.isActive(), newUser.getBalance());
        return userDTO;
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {
        User user = userService.findById(id).orElseThrow();
        URI location = fromCurrentRequest().build().toUri();

        user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        userService.save(user);

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserPutDTO updatedUser) {
        if (userService.exist(id)) {
            User dbUser = userService.findById(id).orElseThrow();

            dbUser.setLogin(updatedUser.getLogin());
            dbUser.setName(updatedUser.getName());
            dbUser.setEncondedPassword(passwordEncoder.encode(updatedUser.getPassword()));
            dbUser.setActive(updatedUser.isActive());
            dbUser.setBalance(updatedUser.getBalance());

            userService.save(dbUser);

            UserDTO userDTO = new UserDTO(dbUser.getId(), dbUser.getLogin(), dbUser.getName(), dbUser.getDate(), dbUser.isActive(), dbUser.getBalance());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else	{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);

            UserDTO userDTO = new UserDTO(user.getId(), user.getLogin(), user.getName(), user.getDate(), user.isActive(), user.getBalance());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteImage(@PathVariable long id) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setImageFile(null);

            userService.save(user);

            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<UserDTO> updateBalance(@PathVariable long id, @RequestParam int balance) {
        if (userService.exist(id)){
            User user = userService.findById(id).orElseThrow();

            if (balance < 0) {
                if (user.isActive() && user.getBalance() >= (balance * -1)) {
                    user.setBalance(doOperation(user.getBalance(), balance));
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                user.setBalance(doOperation(user.getBalance(), balance));
            }

            userService.save(user);

            UserDTO userDTO = new UserDTO(user.getId(), user.getLogin(), user.getName(), user.getDate(), user.isActive(), user.getBalance());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private int doOperation(int userBalance, int balance) {
        return userBalance + balance;
    }
}