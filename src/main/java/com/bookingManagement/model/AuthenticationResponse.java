package com.bookingManagement.model;

import lombok.Getter;

@Getter
public class AuthenticationResponse {
    private String token;
    private String message;

    public AuthenticationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

}
