package com.bookingManagement.dto;

import com.bookingManagement.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class BookingDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("slot")
    private String slot;

    @JsonProperty("user")
    private User user;
}