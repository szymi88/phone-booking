package com.sstankiewicz.phonebooking.services;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.BookingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingsService {
    public Booking getBooking(long id) {
        return new Booking(id, 1, false, "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    }

    public boolean removeBooking(long bookingId) {
        return false;
    }

    public Booking bookPhone(BookingRequest booking) {
        return null;
    }

    public class PhoneBookedException extends RuntimeException{}
}
