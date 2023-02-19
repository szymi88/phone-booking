package com.sstankiewicz.phonebooking.controller;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.BookingRequest;
import com.sstankiewicz.phonebooking.services.BookingsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookingsController.class})
class BookingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingsService bookingsServiceMock;

    @Test
    public void testGetBooking() throws Exception {
        when(bookingsServiceMock.getBooking(1))
                .thenReturn(new Booking(1, 1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().json("""
                                {
                                "bookingId" : 1,
                                "phoneId" : 1,
                                "bookedBy" : "Miles Morales",
                                "bookedFrom" : "2023-01-01T12:00:00"
                                }
                                """));
    }

    @Test
    public void createBooking_expectCreatedResource() throws Exception {
        when(bookingsServiceMock.bookPhone(new BookingRequest(1, "Miles Morales")))
                .thenReturn(new Booking(1, 1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0)));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "phoneId" : 1,
                                "clientName" : "Miles Morales"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().json("""
                                {
                                "bookingId" : 1,
                                "phoneId" : 1,
                                "bookedBy" : "Miles Morales",
                                "bookedFrom" : "2023-01-01T12:00:00"
                                }
                                """));
    }

    @Test
    public void createBooking_expectErrorIfPhoneAlreadyBooked() throws Exception {
        when(bookingsServiceMock.bookPhone(new BookingRequest(1, "Miles Morales")))
                .thenThrow(BookingsService.PhoneBookedException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "phoneId" : 1,
                                "clientName" : "Miles Morales"
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    public void removeExistingBooking_expect204() throws Exception {
        when(bookingsServiceMock.removeBooking(1))
                .thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/bookings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeNotExistingBooking_expect404() throws Exception {
        when(bookingsServiceMock.removeBooking(1))
                .thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/bookings/1"))
                .andExpect(status().isNotFound());
    }
}
