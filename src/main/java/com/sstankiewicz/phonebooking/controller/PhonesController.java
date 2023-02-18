package com.sstankiewicz.phonebooking.controller;


import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.PhoneDetails;
import com.sstankiewicz.phonebooking.model.PhoneHeader;
import com.sstankiewicz.phonebooking.services.PhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/phones")
public class PhonesController {

    private final PhoneService phoneService;

    public PhonesController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping
    public List<PhoneHeader> getPhones(){
        return phoneService.getPhones();
    }

    @GetMapping("/{id}")
    public PhoneDetails getPhoneDetails(@PathVariable long id){
        var phoneDetails =  phoneService.getPhoneDetails(id);
        if (phoneDetails == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return phoneDetails;
    }

    @GetMapping("/{id}/bookings")
    public Booking getPhoneBooking(@PathVariable long id){
        var phoneBooking = phoneService.getPhoneBooking(id);
        if (phoneBooking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return phoneBooking;
    }
}
