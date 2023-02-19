package com.sstankiewicz.phonebooking.services;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.PhoneBookingStatus;
import com.sstankiewicz.phonebooking.model.PhoneDetails;
import com.sstankiewicz.phonebooking.model.PhoneHeader;
import com.sstankiewicz.phonebooking.repository.PhoneEntity;
import com.sstankiewicz.phonebooking.repository.PhoneRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PhoneServiceTest {

    @Test
    void getPhones_shouldReturnAllPhones() {
        var repositoryMock = mock(PhoneRepository.class);
        when(repositoryMock.findAll()).thenReturn(List.of(
                new PhoneEntity(1, "phone1", 0),
                new PhoneEntity(2, "phone2", 0),
                new PhoneEntity(3, "phone3", 0)
        ));

        var service = new PhoneService(repositoryMock, mock(PhoneSpecificationService.class), mock(BookingsService.class));

        assertThat(service.getPhones()).containsExactly(
                new PhoneHeader(1, "phone1"),
                new PhoneHeader(2, "phone2"),
                new PhoneHeader(3, "phone3")
        );
    }

    @Test
    void getPhones_shouldReturnPhoneDetails() {
        var repositoryMock = mock(PhoneRepository.class);
        when(repositoryMock.findById(1)).thenReturn(Optional.of(new PhoneEntity(1, "phone1", 1)));

        var specificationServiceMock = mock(PhoneSpecificationService.class);
        when(specificationServiceMock.getSpecification("phone1"))
                .thenReturn(Optional.of(new PhoneSpecificationService.PhoneSpecification("Samsung S5", "3G", "2gBands", "3gBands", "4gBands")));

        var service = new PhoneService(repositoryMock, specificationServiceMock, mock(BookingsService.class));
        assertThat(service.getPhoneDetails(1))
                .hasValue(new PhoneDetails(1, "phone1", "Samsung S5", "3G", "2gBands", "3gBands", "3gBands"));

    }

    @Test
    void getPhonesBooking_whenAllPhonesBooked_shouldReturnAvailabilityFalseAndDetailsOfLastBooking() {
        var repositoryMock = mock(PhoneRepository.class);
        when(repositoryMock.findById(1)).thenReturn(Optional.of(new PhoneEntity(1, "phone1", 3)));

        var bookingServiceMock = mock(BookingsService.class);
        when(bookingServiceMock.getBookingsByPhoneId(1)).thenReturn(new ArrayList<>(List.of(
                new Booking(1, 1, "Sam Fisher", LocalDateTime.of(2023, 1, 1, 7, 0, 0)),
                new Booking(1, 1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0)),
                new Booking(1, 1, "Peter Parker", LocalDateTime.of(2023, 1, 1, 1, 0, 0))
        )));

        var service = new PhoneService(repositoryMock, mock(PhoneSpecificationService.class), bookingServiceMock);
        assertThat(service.getPhoneBooking(1))
                .hasValue(new PhoneBookingStatus( 1, false,1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0)));

    }

    @Test
    void getPhonesBooking_shouldNotFailIfNoPhoneInStock() {
        var repositoryMock = mock(PhoneRepository.class);
        when(repositoryMock.findById(1)).thenReturn(Optional.of(new PhoneEntity(1, "phone1", 0)));

        var service = new PhoneService(repositoryMock, mock(PhoneSpecificationService.class), mock(BookingsService.class));
        assertThat(service.getPhoneBooking(1))
                .hasValue(new PhoneBookingStatus( 1, false,null, null, null));

    }

    @Test
    void getPhonesBooking_whenAllPhonesAvailable_shouldReturnAvailabilityTrueAndBoBookingDetails() {
        var repositoryMock = mock(PhoneRepository.class);
        when(repositoryMock.findById(1)).thenReturn(Optional.of(new PhoneEntity(1, "phone1", 4)));

        var bookingServiceMock = mock(BookingsService.class);
        when(bookingServiceMock.getBookingsByPhoneId(1)).thenReturn(List.of(
                new Booking(1, 1, "Sam Fisher", LocalDateTime.of(2023, 1, 1, 7, 0, 0)),
                new Booking(1, 1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0)),
                new Booking(1, 1, "Peter Parker", LocalDateTime.of(2023, 1, 1, 1, 0, 0))
        ));

        var service = new PhoneService(repositoryMock, mock(PhoneSpecificationService.class), bookingServiceMock);
        assertThat(service.getPhoneBooking(1))
                .hasValue(new PhoneBookingStatus(1, true, null, null, null));
    }

    @Test
    void getPhonesBooking_whenPhoneDoesnNotExist_shouldReturnEmpty() {
        var repositoryMock = mock(PhoneRepository.class);
        when(repositoryMock.findById(1)).thenReturn(Optional.empty());

        var service = new PhoneService(repositoryMock, mock(PhoneSpecificationService.class), mock(BookingsService.class));
        assertThat(service.getPhoneBooking(1)).isEmpty();
    }
}