package com.bookingManagement.controller;

import com.bookingManagement.dto.AppData;
import com.bookingManagement.dto.BookingDTO;
import com.bookingManagement.dto.ListBookingDTO;
import com.bookingManagement.entities.Booking;
import com.bookingManagement.entities.Data;
import com.bookingManagement.model.User;
import com.bookingManagement.repository.BookingRepository;
import com.bookingManagement.repository.DataRepository;
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

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private MyModelMapper modelMapper;

    @GetMapping(path="/getTodayAvailableBookings")
    public ResponseEntity<ListBookingDTO> getTodayAvailableBookings(){
        try {
            List<Booking> todayBookings = bookingRepository.findAllByDateAndUserIsNull(LocalDate.now());
            ListBookingDTO listBookingDTO = new ListBookingDTO();
            listBookingDTO.setBookings(modelMapper.map(todayBookings, new TypeToken<List<BookingDTO>>(){}.getType()));
            Thread.sleep(2000);
            return ResponseEntity.ok(listBookingDTO);
        }
        catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping(path="/getBookingsByDay")
    public ResponseEntity<?> getBookingsByDay(@RequestParam String date) {
        try {

            LocalDate day = LocalDate.parse(date);
            List<Booking> todayBookings = bookingRepository.findAllByDateAndUserIsNull(day);
            ListBookingDTO listBookingDTO = new ListBookingDTO();
            listBookingDTO.setBookings(modelMapper.map(todayBookings, new TypeToken<List<BookingDTO>>(){}.getType()));
            return ResponseEntity.ok(listBookingDTO);

        } catch (DateTimeParseException e) {
            log.info("Invalid date format: {}", date, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format: " + date);
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @GetMapping("/getBooking/{id}")
    public ResponseEntity<?> getBooking(@PathVariable Long id) {
        try {
            Optional<Booking> bookingOptional = bookingRepository.findByIdAndUserIsNull(id);
            if (bookingOptional.isPresent()) {
                Booking booking = bookingOptional.get();
                BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
                return ResponseEntity.ok(bookingDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found with ID: " + id);
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/app-data")
    public ResponseEntity<AppData> getAppData() {
        try {
            Optional<Data> data = dataRepository.findById(1L);
            if(data.isPresent())
                return ResponseEntity.ok(new AppData(data.get().getNum(),data.get().isChiuso(), false));
            else{
                Data newData = new Data(1L,1,true);
                dataRepository.save(newData);
                return ResponseEntity.ok(new AppData(0,false, false));
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("/updateApp-data")
    public ResponseEntity<?> updateAppData(@RequestBody AppData appData) {
        try {
            Data newData = new Data(1L,appData.getInitialCount(),appData.isClose());
            dataRepository.save(newData);
            appData.setError(false);
            return ResponseEntity.ok(appData);
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}