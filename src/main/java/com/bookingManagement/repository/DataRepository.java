package com.bookingManagement.repository;

import com.bookingManagement.entities.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DataRepository extends JpaRepository<Data, Long>, JpaSpecificationExecutor<Data> {
    Optional<Data> findById(Long Id);
}