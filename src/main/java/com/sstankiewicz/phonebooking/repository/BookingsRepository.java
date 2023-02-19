package com.sstankiewicz.phonebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingsRepository extends JpaRepository<BookingEntity, Integer> {
    int countByPhoneId(int phoneId);
}
