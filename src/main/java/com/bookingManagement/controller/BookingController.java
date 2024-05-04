package com.bookingManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class BookingController {

    @GetMapping(path="/user")
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("Hello user");
    }
    @GetMapping(path="/admin")
    public ResponseEntity<String> getAdmin(){
        return ResponseEntity.ok("Hello admin");
    }
}
