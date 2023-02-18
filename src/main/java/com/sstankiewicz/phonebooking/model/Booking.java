package com.sstankiewicz.phonebooking.model;

import java.time.LocalDateTime;

public record Booking(long bookingId, long phoneId, boolean availability, String bookedBy, LocalDateTime bookedFrom){};