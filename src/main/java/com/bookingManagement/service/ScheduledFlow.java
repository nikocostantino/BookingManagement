package com.bookingManagement.service;

import com.bookingManagement.entities.Booking;
import com.bookingManagement.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Slf4j
@Service
public class ScheduledFlow {

    private final static String[] slots = {"09:00-09:30","09:30-10:00","10:00-10:30","10:30-11:00","11:00-11:30","11:30-12:00","12:00-12:30","12:30-13:00"};

    @Autowired
    private BookingRepository bookingRepository;

    @Scheduled(cron ="${cron.expression.insertFlow}", zone = "${Europe/Rome}")
    @Bean
    //@Scope("prototype")
    public void insertRecordInDB(){
        LocalDate now = LocalDate.now();
        LocalDate nowPluMonth = now.plusMonths(1);
        while(!now.equals(nowPluMonth)) {

            for(String slot : slots){
                Booking booking = new Booking();
                booking.setDate(now);
                booking.setSlot(slot);
                bookingRepository.save(booking);
            }
            now = now.plusDays(1);
            log.info(now.toString());
        }
    }
}
