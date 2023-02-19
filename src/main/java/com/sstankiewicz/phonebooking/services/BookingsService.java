package com.sstankiewicz.phonebooking.services;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.BookingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingsService {
    public Booking getBooking(int id) {
        return new Booking(id, 1,  "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    }

    public List<Booking> getBookingsByPhoneId(int id) {
        return List.of(new Booking(id, 1,  "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0)));
    }

    public boolean removeBooking(long bookingId) {
        return false;
    }

    public Booking bookPhone(BookingRequest booking) {
        return null;
    }

    public static class PhoneBookedException extends RuntimeException{}
}
