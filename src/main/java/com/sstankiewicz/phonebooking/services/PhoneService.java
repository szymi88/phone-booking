package com.sstankiewicz.phonebooking.services;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.PhoneBookingStatus;
import com.sstankiewicz.phonebooking.model.PhoneDetails;
import com.sstankiewicz.phonebooking.model.PhoneHeader;
import com.sstankiewicz.phonebooking.repository.PhoneEntity;
import com.sstankiewicz.phonebooking.repository.PhoneRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneService {

    private final PhoneRepository phoneRepository;

    private final PhoneSpecificationService phoneApiClient;

    private final BookingsService bookingsService;

    public PhoneService(PhoneRepository phoneRepository, PhoneSpecificationService phoneApiClient, BookingsService bookingsService) {
        this.phoneRepository = phoneRepository;
        this.phoneApiClient = phoneApiClient;
        this.bookingsService = bookingsService;
    }

    public List<PhoneHeader> getPhones() {
        return phoneRepository.findAll()
                .stream()
                .map(phoneEntity -> new PhoneHeader(phoneEntity.getId(), phoneEntity.getPhoneName()))
                .toList();
    }

    public Optional<PhoneBookingStatus> getPhoneBooking(int id) {
        return phoneRepository.findById(id).map(this::resolvePhoneBookingStatus);
    }

    private PhoneBookingStatus resolvePhoneBookingStatus(PhoneEntity phoneEntity) {
        if (phoneEntity.getStockCount() < 1) {
            return new PhoneBookingStatus(phoneEntity.getId(), false, null, null, null);
        }

        var bookings = bookingsService.getBookingsByPhoneId(phoneEntity.getId());
        if (bookings.size() < phoneEntity.getStockCount()) {
            return new PhoneBookingStatus(phoneEntity.getId(), true, null, null, null);
        }
        bookings.sort(Comparator.comparing(Booking::bookedFrom));
        var lastBooking = bookings.get(bookings.size() - 1);
        return new PhoneBookingStatus(phoneEntity.getId(), false, lastBooking.bookingId(), lastBooking.bookedBy(), lastBooking.bookedFrom());
    }

    public Optional<PhoneDetails> getPhoneDetails(int id) {
        return phoneRepository.findById(id)
                .map(phoneEntity -> phoneApiClient.getSpecification(phoneEntity.getPhoneName()).map(
                                phoneSpecification -> toPhoneDetails(phoneEntity, phoneSpecification)
                        )
                        .orElse(new PhoneDetails(phoneEntity.getId(), phoneEntity.getPhoneName(), null, null, null, null, null)));
    }

    private static PhoneDetails toPhoneDetails(PhoneEntity phoneEntity, PhoneSpecificationService.PhoneSpecification phoneSpecification) {
        return new PhoneDetails(phoneEntity.getId(), phoneEntity.getPhoneName(), phoneSpecification.model(), phoneSpecification.technology(), phoneSpecification._2gBands(), phoneSpecification._3gBands(), phoneSpecification._3gBands());
    }
}
