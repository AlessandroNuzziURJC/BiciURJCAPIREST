package group12.biciurjc.controller;

import group12.biciurjc.model.DTO.UserDTO;
import group12.biciurjc.model.User;
import group12.biciurjc.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
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

    //Get all user
    @GetMapping("/")
    public List<User> getUsers(){
        return userService.findAll();
    }

    //Get user with id
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        Optional<User> user = userService.findById(id);

        if (user.isPresent()){

            UserDTO userDTO = new UserDTO(user.get().getName(), user.get().getId(), user.get().getBalance());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Get user image
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

    //Post user
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody User user) {

        User newUser = new User(user.getName(), passwordEncoder.encode(user.getEncondedPassword()), user.getBalance());
        UserDTO userDTO = new UserDTO(user.getName(), user.getId(), user.getBalance());

        userService.save(newUser);

        return userDTO;
    }

    //Post user image
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        User user = userService.findById(id).orElseThrow();

        URI location = fromCurrentRequest().build().toUri();

        user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        userService.save(user);

        return ResponseEntity.created(location).build();
    }


    //Put user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User updatedUser) throws SQLException {
        if (userService.exist(id)) {
            User dbUser = userService.findById(id).orElseThrow();

            if (updatedUser.getImageFile() != null) {
                if (dbUser.getImageFile() != null) {
                    updatedUser.setImageFile(BlobProxy.generateProxy(dbUser.getImageFile().getBinaryStream(),
                            dbUser.getImageFile().length()));
                }
            }

            updatedUser.setNewDate(dbUser.getDate());
            updatedUser.setId(id);
            userService.save(updatedUser);
            UserDTO userDTO = new UserDTO(updatedUser.getName(), updatedUser.getId(), updatedUser.getBalance());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else	{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //Delete user image
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteImage(@PathVariable long id) throws IOException {

        User user = userService.findById(id).orElseThrow();

        user.setImageFile(null);

        userService.save(user);

        return ResponseEntity.noContent().build();
    }

}