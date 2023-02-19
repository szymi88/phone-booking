package com.sstankiewicz.phonebooking.services;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhoneSpecificationService {
    public record PhoneSpecification(String model, String technology, String _2gBands, String _3gBands, String _4gBands) {}

    public Optional<PhoneSpecification> getSpecification(String name) {
        return null;
    }

}
