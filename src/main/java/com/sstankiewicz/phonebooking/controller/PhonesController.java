package com.sstankiewicz.phonebooking.controller;


import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.PhoneBookingStatus;
import com.sstankiewicz.phonebooking.model.PhoneDetails;
import com.sstankiewicz.phonebooking.model.PhoneHeader;
import com.sstankiewicz.phonebooking.services.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
            @ApiResponse(responseCode = "200", description = "Returns list of phones", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PhoneHeader.class))))
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
            @ApiResponse(responseCode = "200", description = "Returns phone details", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Booking.class))}),
            @ApiResponse(responseCode = "404", description = "Phone id doesn't exit", content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public PhoneDetails getPhoneDetails(@PathVariable @Parameter(example = "1") int id) {
        return phoneService.getPhoneDetails(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Provides phone's booking status
     *
     * @param id id of the phone
     * @return phone's booking details or 404 if phone id doesn't exit
     */
    @Operation(summary = "Get booking status of the phone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns booking status", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PhoneBookingStatus.class))}),
            @ApiResponse(responseCode = "404", description = "Phone id doesn't exit", content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}/bookings")
    public PhoneBookingStatus getPhoneBooking(@PathVariable @Parameter(example = "1") int id) {
        return phoneService.getPhoneBooking(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
