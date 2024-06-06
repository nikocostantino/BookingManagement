package com.bookingManagement.service;

import com.bookingManagement.repository.BookingRepository;
import com.bookingManagement.repository.UserRepository;
import com.bookingManagement.util.LockManager;
import com.bookingManagement.util.MyModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.Optional;

@Slf4j
@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyModelMapper modelMapper;

    private final LockManager lockManager = new LockManager();

    public void bookSlot(Integer userId, Long bookingId) {
        Object lock = lockManager.getLock(bookingId);
        synchronized (lock) {
            var booking = bookingRepository.findById(bookingId);
            if(booking.isEmpty()){
                lockManager.releaseLock(bookingId);
                throw new NullPointerException("bookingId not found");
            }
            if(booking.get().getUser()==null){
                var user = userRepository.findById(userId);
                if(user.isPresent()){
                    booking.get().setUser(user.get());
                    bookingRepository.save(booking.get());
                    log.info("Updating booking " + bookingId + " with new user: " + userId);
                    // Dopo l'aggiornamento puoi rilasciare il lock se necessario
                }
                lockManager.releaseLock(bookingId);
            }
            else {
                if(booking.get().getUser().getId().equals(userId)){
                    lockManager.releaseLock(bookingId);
                    log.info("Slot already booked with new user: {}", userId);
                }
                else{
                    lockManager.releaseLock(bookingId);
                    throw new IllegalArgumentException("slot is not bookable");
                }
            }
        }
    }

    public void cancelBooking(Integer userId, Long bookingId) {
        var booking = bookingRepository.findById(bookingId);
        if(booking.isEmpty()){
            throw new NullPointerException("bookingId not found");
        }
        if(booking.get().getUser()==null){
            throw new IllegalStateException();
        }
        else {
            if(booking.get().getUser().getId().equals(userId)){
                booking.get().setUser(null);
                bookingRepository.save(booking.get());
                log.info("Slot cancelled from user: {}", userId);
            }
            else{
                throw new IllegalArgumentException("slot is not cancellable by: "+userId);
            }
        }
    }
}
