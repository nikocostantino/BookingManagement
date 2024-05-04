package com.bookingManagement.controller;

import com.bookingManagement.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @GetMapping(path="/getAllAvailableBookings")
    public ResponseEntity<String> getAllBookings(@RequestParam Optional<String> date){
        // Ottenere l'oggetto Authentication dal SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Controllare se l'utente è autenticato
        if (authentication != null && authentication.isAuthenticated()) {
            // Ottenere il token di autenticazione
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                String username = ((User) principal).getUsername();
                log.info("utente autenticato: "+username);
                //TODO in base all'utente prendo quelle disponibili, cioe quelle vuote e quelle in cui c'è lui
            }
        }
        return ResponseEntity.ok("All the available bookings");
    }

}