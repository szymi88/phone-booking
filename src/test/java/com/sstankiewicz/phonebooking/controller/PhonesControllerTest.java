package com.sstankiewicz.phonebooking.controller;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.PhoneBookingStatus;
import com.sstankiewicz.phonebooking.model.PhoneDetails;
import com.sstankiewicz.phonebooking.model.PhoneHeader;
import com.sstankiewicz.phonebooking.services.PhoneService;
import com.sstankiewicz.phonebooking.services.PhoneSpecificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PhonesController.class)
class PhonesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhoneService phoneService;

    @MockBean
    private PhoneSpecificationService pp;

    @Test
    public void getPhones_ExpectListOfAllPhones() throws Exception {
        when(phoneService.getPhones())
                .thenReturn(List.of(new PhoneHeader(1, "Samsung Galaxy S9"), new PhoneHeader(2, "Oneplus 9")));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/phones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().json("""
                                [
                                  {
                                   "id" : 1,
                                   "name" : "Samsung Galaxy S9"
                                  },
                                  {
                                   "id" : 2,
                                   "name" : "Oneplus 9"
                                  }
                                ]
                                """));
    }

    @Test
    public void getPhone_ExpectPhoneDetails() throws Exception {
        when(phoneService.getPhoneDetails(1))
                .thenReturn(Optional.of(new PhoneDetails(1, "Samsung Galaxy S9", "Samsung Galaxy S9", "Tech", "2gBand", "3gBand", "4gBand")));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/phones/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().json("""
                                  {
                                   "id" : 1,
                                   "name" : "Samsung Galaxy S9",
                                   "model": "Samsung Galaxy S9",
                                   "technology": "Tech",
                                   "2gBands": "2gBand",
                                   "3gBands": "3gBand",
                                   "4gBands": "4gBand"
                                  }
                                """));
    }

    @Test
    public void getPhone_phoneDoesNotExist_expect404() throws Exception {
        when(phoneService.getPhoneDetails(1)).thenReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/phones/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPhoneBooking_ExpectBookingInformation() throws Exception {
        when(phoneService.getPhoneBooking(1))
                .thenReturn(Optional.of(new PhoneBookingStatus(1, false, 1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0))));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/phones/1/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().json("""
                              {
                                 "phoneId" : 1,
                                 "availability" : false,
                                 "bookingId" : 1,
                                 "bookedBy" : "Miles Morales",
                                 "bookedFrom" : "2023-01-01T12:00:00"
                                }
                              """));
    }

    @Test
    public void getPhoneBooking_phoneDoesNotExist_expect404() throws Exception {
        when(phoneService.getPhoneBooking(1))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/phones/1/bookings"))
                .andExpect(status().isNotFound());
    }

}