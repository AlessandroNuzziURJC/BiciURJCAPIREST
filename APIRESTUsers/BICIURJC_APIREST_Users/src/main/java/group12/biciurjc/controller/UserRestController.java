package group12.biciurjc.controller;

import group12.biciurjc.model.DTO.UserDTO;
import group12.biciurjc.model.DTO.UserPostDTO;
import group12.biciurjc.model.DTO.UserPutDTO;
import group12.biciurjc.model.DTO.BalanceDTO;
import group12.biciurjc.model.User;
import group12.biciurjc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Devuelve todos los usuarios de la aplicación")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "La lista de usuarios se ha devuelto correctamente",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation=UserDTO.class))
                    )}
            )
    })
    @GetMapping("/")
    public List<UserDTO> getUsers() {
        List<UserDTO> usersDTO = new ArrayList<>();

        for (User user : userService.findAll()) {
            usersDTO.add(new UserDTO(user.getId(), user.getLogin(), user.getName(), user.getDate(), user.isActive(), user.getBalance()));
        }

        return usersDTO;
    }

    @Operation(summary = "Devuelve el usuario solicitado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "El usuario se ha encontrado y devuelto correctamente",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=UserDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El usuario no se ha encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@Parameter(description = "id del usuario") @PathVariable long id) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO(user.getId(), user.getLogin(), user.getName(), user.getDate(), user.isActive(), user.getBalance());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Devuelve la imagen del usuario solicitado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "El usuario se ha encontrado y la imagen se ha devuelto correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El usuario no se ha encontrado o no tiene imagen",
                    content = @Content
            )
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@Parameter(description = "id del usuario") @PathVariable long id) throws SQLException {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getImageFile() != null) {
                Resource file = new InputStreamResource(user.getImageFile().getBinaryStream());

                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .contentLength(user.getImageFile().length()).body(file);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crea un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "El usuario se ha creado correctamente",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=UserDTO.class)
                    )}
            )
    })
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Parameter(description = "nuevo usuario") @RequestBody UserPostDTO user) {
        User newUser = new User(user.getLogin(), user.getName(), passwordEncoder.encode(user.getPassword()));
        userService.save(newUser);

        UserDTO userDTO = new UserDTO(newUser.getId(), newUser.getLogin(), newUser.getName(), newUser.getDate(), newUser.isActive(), newUser.getBalance());
        return userDTO;
    }

    @Operation(summary = "Actualiza la imagen del usuario solicitado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "La imagen se ha guardado correctamente",
                    content = @Content
            )
    })
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadImage(@Parameter(description = "id del usuario") @PathVariable long id,
                                              @Parameter(description = "imagen") @RequestParam MultipartFile imageFile) throws IOException {

        User user = userService.findById(id).orElseThrow();
        URI location = fromCurrentRequest().build().toUri();

        user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        userService.save(user);

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Actualiza el usuario solicitado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "El usuario se ha actualizado correctamente",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=UserDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El usuario no se ha encontrado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@Parameter(description = "id del usuario") @PathVariable long id,
                                              @Parameter(description = "usuario con los campos a actualizar") @RequestBody UserPutDTO updatedUser) {

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

    @Operation(summary = "Elimina el usuario solicitado, quedando este como inactivo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "El usuario se ha eliminado correctamente",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=UserDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El usuario no se ha encontrado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@Parameter(description = "id del usuario") @PathVariable long id) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            userService.save(user);

            UserDTO userDTO = new UserDTO(user.getId(), user.getLogin(), user.getName(), user.getDate(), user.isActive(), user.getBalance());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Elimina la imagen del usuario solicitado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "La imagen del usuario se ha eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El usuario no se ha encontrado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteImage(@Parameter(description = "id del usuario") @PathVariable long id) {
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

    @Operation(summary = "Actualiza el saldo del usuario solicitado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "El saldo del usuario se ha actualizado correctamente",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=UserDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El usuario no se ha encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario no está activo o no tiene suficiente dinero para alquilar la bicicleta",
                    content = @Content
            )
    })
    @PutMapping("/{id}/balance")
    public ResponseEntity<UserDTO> updateBalance(@Parameter(description = "id del usuario") @PathVariable long id,
                                                 @Parameter(description = "dinero a añadir o quitar del saldo del usuario") @RequestBody BalanceDTO balance) {
        if (userService.exist(id)){
            User user = userService.findById(id).orElseThrow();

            if (balance.getBalance() < 0) {
                if (user.isActive() && user.getBalance() >= (balance.getBalance() * -1)) {
                    user.setBalance(doOperation(user.getBalance(), balance.getBalance()));
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                user.setBalance(doOperation(user.getBalance(), balance.getBalance()));
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