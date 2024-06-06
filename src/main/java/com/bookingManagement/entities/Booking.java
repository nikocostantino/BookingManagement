package com.bookingManagement.entities;

import com.bookingManagement.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="BOOKING")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DATE")
    private LocalDate date;
    @Column(name = "SLOT")
    private String slot;
    @ManyToOne
    @JoinColumn(name="USER_ID",referencedColumnName = "id")
    private User user;
}
