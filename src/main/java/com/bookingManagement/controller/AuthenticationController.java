package com.bookingManagement.controller;

import com.bookingManagement.model.AuthenticationResponse;
import com.bookingManagement.model.User;
import com.bookingManagement.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login( @RequestBody User request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PutMapping("/update")
    public ResponseEntity<AuthenticationResponse> update(@RequestBody User updatedUser) {
        // Ottenere l'oggetto Authentication dal SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Controllare se l'utente è autenticato
        if (authentication != null && authentication.isAuthenticated()) {
            // Ottenere il token di autenticazione
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                String username = ((User) principal).getUsername();
                if(!Objects.equals(username, updatedUser.getUsername())){
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        }
        AuthenticationResponse response = authService.update(updatedUser);
        if (response.getToken() == null) {
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<AuthenticationResponse> delete(@PathVariable String username) {
        // Ottenere l'oggetto Authentication dal SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Controllare se l'utente è autenticato
        if (authentication != null && authentication.isAuthenticated()) {
            // Ottenere il token di autenticazione
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                String usernameInDb = ((User) principal).getUsername();
                if(!Objects.equals(usernameInDb, username)){
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        }
        AuthenticationResponse response = authService.delete(username);
        if (response.getToken() == null) {
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
