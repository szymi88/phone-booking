package com.sstankiewicz.phonebooking.controller;


import com.sstankiewicz.phonebooking.model.PhoneBookingStatus;
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
import java.util.Optional;

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
    public Optional<PhoneDetails> getPhoneDetails(@PathVariable int id){
        if (phoneService.getPhoneDetails(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return phoneService.getPhoneDetails(id);
    }

    @GetMapping("/{id}/bookings")
    public Optional<PhoneBookingStatus> getPhoneBooking(@PathVariable int id){
        var phoneBooking = phoneService.getPhoneBooking(id);
        if (phoneBooking.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return phoneBooking;
    }
}
