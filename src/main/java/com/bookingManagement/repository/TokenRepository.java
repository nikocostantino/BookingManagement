package com.bookingManagement.repository;

import com.bookingManagement.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {


    @Query("SELECT t FROM Token t INNER JOIN User u on t.user.id = u.id WHERE t.user.id = :userId and t.loggedOut = false")
    List<Token> findAllTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
