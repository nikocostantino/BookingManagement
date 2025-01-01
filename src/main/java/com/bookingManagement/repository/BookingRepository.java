package com.bookingManagement.repository;

import com.bookingManagement.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    //TODO sistemare in base all'orario, se sono le 14 non mostrare quelle prima
    List<Booking> findAllByDateAndUserIsNull(LocalDate date);

    List<Booking> findAllByDateAndUserIsNotNull(LocalDate day);

    Optional<Booking> findByIdAndUserIsNull(Long id);

/*    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.user.id = :userId WHERE b.id = :bookingId AND b.user.id IS NULL")
    void bookSlot(Integer userId, Long bookingId);

    @Query("SELECT CASE WHEN (u.user.id IS NULL) THEN TRUE ELSE FALSE END FROM Booking u WHERE u.id = :id")
    boolean isItBookable(Long bookingId);*/
}
