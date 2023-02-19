package com.sstankiewicz.phonebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<BookingEntity, Integer> {
    int countByPhoneId(int phoneId);
    List<BookingEntity> findByPhoneId(int phoneId);


}
