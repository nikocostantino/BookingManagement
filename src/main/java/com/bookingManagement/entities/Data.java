package com.bookingManagement.entities;

import com.bookingManagement.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@lombok.Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="DATA")
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NUM")
    private int num;
    @Column(name = "CHIUSO")
    private boolean chiuso;
}