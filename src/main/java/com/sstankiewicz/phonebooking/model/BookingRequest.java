package com.sstankiewicz.phonebooking.model;

import io.swagger.v3.oas.annotations.Parameter;

public record BookingRequest(@Parameter(example = "1") int phoneId, @Parameter(example = "John Smith") String clientName){}
