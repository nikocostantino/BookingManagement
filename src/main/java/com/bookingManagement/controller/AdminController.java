package com.bookingManagement.controller;

import com.bookingManagement.dto.BookingDTO;
import com.bookingManagement.dto.ListBookingDTO;
import com.bookingManagement.dto.NotesDTO;
import com.bookingManagement.entities.Booking;
import com.bookingManagement.repository.BookingRepository;
import com.bookingManagement.service.AuthenticationService;
import com.bookingManagement.service.BookingService;
import com.bookingManagement.util.MyModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private MyModelMapper modelMapper;

    @PutMapping(path="/bookSlot")
    public ResponseEntity<String> bookSlot(@RequestParam Long bookingId,@RequestParam Integer userId, @RequestBody Optional<NotesDTO> notes){
        try{
            bookingService.bookSlot(userId, bookingId, notes);
            return ResponseEntity.ok("Slot booked");
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
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking cancelled");
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
    @GetMapping(path="/getAllBookings")
    public ResponseEntity<?> getAllBookings(@RequestParam String date){
        try {

            LocalDate day = LocalDate.parse(date);
            List<Booking> bookings = bookingRepository.findAllByDateAndUserIsNotNull(day);
            ListBookingDTO listBookingDTO = new ListBookingDTO();
            listBookingDTO.setBookings(modelMapper.map(bookings, new TypeToken<List<BookingDTO>>(){}.getType()));
            return ResponseEntity.ok(listBookingDTO);

        } catch (DateTimeParseException e) {
            log.info("Invalid date format: {}", date, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format: " + date);
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}