package com.sstankiewicz.phonebooking.model;

import java.time.LocalDateTime;

public record PhoneBookingStatus(int phoneId, boolean availability, Integer bookingId, String bookedBy, LocalDateTime bookedFrom){}