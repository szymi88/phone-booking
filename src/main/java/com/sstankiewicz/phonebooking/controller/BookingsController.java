package com.sstankiewicz.phonebooking.controller;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.BookingRequest;
import com.sstankiewicz.phonebooking.services.BookingsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Controller for the /bookings endpoint
 * Provides REST endpoints for CRUD operations related to booking a phone
 *
 * @author sstankiewicz
 */
@RestController
@RequestMapping("/bookings")
public class BookingsController {

    private final BookingsService bookingsService;

    public BookingsController(BookingsService bookingsService) {
        this.bookingsService = bookingsService;
    }

    /**
     * Returns booking by id
     *
     * @param bookingId id of the booking
     * @return booking details or 404 if booking doesn't exit
     */
    @GetMapping(value = "/{bookingId}")
    public Optional<Booking> getBooking(@PathVariable int bookingId) {
        return bookingsService.getBooking(bookingId);
    }

    /**
     * Allows to book phone
     *
     * @param booking booking details
     * @return booking details or 409 if phone is out of stock and can't be booked
     */
    @PostMapping
    public Optional<Booking> bookPhone(@RequestBody BookingRequest booking) {
        try {
            return bookingsService.bookPhone(booking);
        } catch (BookingsService.PhoneBookedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    /**
     * Allows to return the phone by removing the relevant booking by its ID
     *
     * @param bookingId id of the booking
     */
    @DeleteMapping(value = "/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnPhone(@PathVariable int bookingId) {
        bookingsService.removeBooking(bookingId);
    }

}
