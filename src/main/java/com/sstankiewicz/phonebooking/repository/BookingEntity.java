package com.sstankiewicz.phonebooking.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHONE_ID")
    PhoneEntity phone;
    String bookedBy;
    LocalDateTime bookedFrom;
}
