package com.bookingManagement.controller;

import com.bookingManagement.model.Role;
import com.bookingManagement.model.User;
import com.bookingManagement.repository.BookingRepository;
import com.bookingManagement.service.AuthenticationService;
import com.bookingManagement.service.BookingService;
import com.bookingManagement.util.MyModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private AuthenticationService authenticationService;

    @PutMapping(path="/bookSlot")
    public ResponseEntity<String> bookSlot(@RequestParam Long bookingId){
        try{
            User user = authenticationService.getAuthenticatedUser();
            String username = user.getUsername();
            Integer userId = user.getId();
            Role role = user.getRole();
            log.info("utente autenticato: " + username);

            if (role.equals(Role.USER)) {
                bookingService.bookSlot(userId, bookingId);
                return ResponseEntity.ok("Slot booked");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

        } catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @PutMapping(path="/cancelBooking")
    public ResponseEntity<String> cancelBooking(@RequestParam Long bookingId){
        try{
            User user = authenticationService.getAuthenticatedUser();
            String username = user.getUsername();
            Integer userId = user.getId();
            Role role = user.getRole();
            log.info("utente autenticato: " + username);

            if (role.equals(Role.USER)) {
                bookingService.cancelBooking(userId, bookingId);
                return ResponseEntity.ok("Slot cancelled");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

        } catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
