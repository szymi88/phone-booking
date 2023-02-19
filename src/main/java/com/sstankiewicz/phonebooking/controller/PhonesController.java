package com.sstankiewicz.phonebooking.controller;


import com.sstankiewicz.phonebooking.model.PhoneBookingStatus;
import com.sstankiewicz.phonebooking.model.PhoneDetails;
import com.sstankiewicz.phonebooking.model.PhoneHeader;
import com.sstankiewicz.phonebooking.services.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Controller for the /bookings endpoint
 * Provides REST endpoints to find available phones, their details and booking status
 *
 * @author sstankiewicz
 */
@RestController
@RequestMapping("/phones")
public class PhonesController {

    private final PhoneService phoneService;

    public PhonesController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    /**
     * Returns a list of all the available phones
     *
     * @return list of phones
     */
    @Operation(summary = "Get available phones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of phones", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping
    public List<PhoneHeader> getPhones() {
        return phoneService.getPhones();
    }

    /**
     * Provides phone details
     *
     * @param id id of the phone
     * @return phone specification 404 if phone id doesn't exit
     */
    @Operation(summary = "Get phone details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns phone details", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Phone id doesn't exit")
    })
    @GetMapping("/{id}")
    public Optional<PhoneDetails> getPhoneDetails(@PathVariable @Parameter(example = "1") int id) {
        if (phoneService.getPhoneDetails(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return phoneService.getPhoneDetails(id);
    }

    /**
     * Provides phone's booking status
     *
     * @param id id of the phone
     * @return phone's booking details or 404 if phone id doesn't exit
     */
    @Operation(summary = "Get booking status of the phone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns booking status", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Phone id doesn't exit")
    })
    @GetMapping("/{id}/bookings")
    public Optional<PhoneBookingStatus> getPhoneBooking(@PathVariable @Parameter(example = "1") int id) {
        var phoneBooking = phoneService.getPhoneBooking(id);
        if (phoneBooking.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return phoneBooking;
    }
}
