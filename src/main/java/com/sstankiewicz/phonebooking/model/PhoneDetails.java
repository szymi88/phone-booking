package com.sstankiewicz.phonebooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhoneDetails(
        int id,
        String name,
        String model,
        String technology,
        @JsonProperty("2gBands") String _2gBands,
        @JsonProperty("3gBands") String _3gBands,
        @JsonProperty("4gBands") String _4gBands) {
}
