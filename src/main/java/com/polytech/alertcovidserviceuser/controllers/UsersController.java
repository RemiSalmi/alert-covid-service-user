package com.polytech.alertcovidserviceuser.controllers;

import com.polytech.alertcovidserviceuser.models.KeycloakUser;
import com.polytech.alertcovidserviceuser.models.User;
import com.polytech.alertcovidserviceuser.models.UserRepository;
import com.polytech.alertcovidserviceuser.services.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users/")
public class UsersController {
    @Autowired
    private UserRepository userRepository;
    @Value("${KEYCLOAK_URL}")
    private String serverUrl;
    @Value("${KEYCLOAK_REALM}")
    private String realm;
    @Value("${KEYCLOAK_ADMIN}")
    private String adminUsername;
    @Value("${KEYCLOAK_ADMIN_PASSWORD}")
    private String adminPassword;

    @GetMapping
    public List<User> list() {
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{email}")
    public User get(@PathVariable String email) {
        User user = userRepository.getUserByEmail(email);
        if(user != null){
            return user;
        }else{
            throw new ResponseStatusException( HttpStatus .NOT_FOUND, "User with email "+email+" not found" ) ;
        }

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody final KeycloakUser keycloakUser ) {
        KeycloakService keycloakService = new KeycloakService(serverUrl, realm, adminUsername, adminPassword);
        User user = new User();
        user.setId_user(keycloakUser.getId_user());
        user.setEmail(keycloakUser.getEmail());
        user.setFirstname(keycloakUser.getFirstname());
        user.setLastname(keycloakUser.getLastname());
        user.setPhone(keycloakUser.getPhone());
        if(userRepository.getUserByEmail(user.getEmail()) == null){
            if(keycloakUser.getPassword() != null){
                String keycloakRegister = keycloakService.createAgentOnKeyclock(user.getFirstname(),user.getLastname(),user.getEmail(),keycloakUser.getPassword());
                User savedUser = userRepository.saveAndFlush(user);
                if(keycloakRegister.equals("Success") && savedUser != null){
                    return savedUser;
                }else{
                    throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "can't register the user" ) ;
                }
            }else{
                throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "password can't be empty" ) ;
            }

        }else{
            throw new ResponseStatusException( HttpStatus.CONFLICT, "user already exist" ) ;
        }


    }

}
