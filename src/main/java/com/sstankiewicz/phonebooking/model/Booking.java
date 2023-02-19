package com.sstankiewicz.phonebooking.model;

import java.time.LocalDateTime;

public record Booking(int bookingId, int phoneId, String bookedBy, LocalDateTime bookedFrom){}