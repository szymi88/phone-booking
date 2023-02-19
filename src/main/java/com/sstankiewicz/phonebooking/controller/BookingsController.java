package com.sstankiewicz.phonebooking.controller;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.BookingRequest;
import com.sstankiewicz.phonebooking.services.BookingsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingsController {

    private final BookingsService bookingsService;

    public BookingsController(BookingsService bookingsService) {
        this.bookingsService = bookingsService;
    }

    @GetMapping(value = "/{bookingId}")
    public Optional<Booking> getBooking(@PathVariable int bookingId) {
        return bookingsService.getBooking(bookingId);
    }

    @PostMapping
    public Optional<Booking> bookPhone(@RequestBody BookingRequest booking) {
        try {
            return bookingsService.bookPhone(booking);
        } catch (BookingsService.PhoneBookedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnPhone(@PathVariable int bookingId) {
        bookingsService.removeBooking(bookingId);
    }

}
