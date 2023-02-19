package com.sstankiewicz.phonebooking.services;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.BookingRequest;
import com.sstankiewicz.phonebooking.repository.BookingEntity;
import com.sstankiewicz.phonebooking.repository.BookingsRepository;
import com.sstankiewicz.phonebooking.repository.PhoneEntity;
import com.sstankiewicz.phonebooking.repository.PhoneRepository;
import org.hibernate.TransientPropertyValueException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingsServiceTest {

    private final static Clock fixedClock = Clock.fixed(LocalDateTime.of(2023, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC.normalized());

    @Test
    void getBooking() {
        var bookingsRepositoryMock = mock(BookingsRepository.class);

        var service = new BookingsService(bookingsRepositoryMock, mock(PhoneRepository.class), fixedClock);
        service.getBooking(1);
    }

    @Test
    void getBookingsByPhoneId() {
        var bookingsRepositoryMock = mock(BookingsRepository.class);

        when(bookingsRepositoryMock.findByPhoneId(1))
                .thenReturn(List.of(
                        new BookingEntity(1, new PhoneEntity(1, null, 1), "Miles Morales", LocalDateTime.of(2023, 1, 1, 0, 0, 0))
                ));
        var service = new BookingsService(bookingsRepositoryMock, mock(PhoneRepository.class), fixedClock);

        assertThat(service.getBookingsByPhoneId(1)).containsExactly(new Booking(1, 1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 0, 0, 0)));

    }

    @Test
    void removeBooking_shouldRemoveBookingFromTheDb() {
        var bookingsRepositoryMock = mock(BookingsRepository.class);

        doNothing().when(bookingsRepositoryMock).deleteById(1);

        var service = new BookingsService(bookingsRepositoryMock, mock(PhoneRepository.class), fixedClock);
        service.removeBooking(1);

        verify(bookingsRepositoryMock, times(1)).deleteById(1);
    }

    @Test
    void bookPhone() {
        var bookingsRepositoryMock = mock(BookingsRepository.class);
        when(bookingsRepositoryMock.save(any()))
                .thenAnswer(invocation -> {
                    var arg = invocation.getArgument(0, BookingEntity.class);
                    arg.setId(1);
                    return arg;
                });

        when(bookingsRepositoryMock.countByPhoneId(1)).thenReturn(1);

        var phoneRepositoryMock = mock(PhoneRepository.class);
        when(phoneRepositoryMock.findById(1)).thenReturn(Optional.of(new PhoneEntity(1, null, 2)));

        var service = new BookingsService(bookingsRepositoryMock, phoneRepositoryMock, fixedClock);

        assertThat(service.bookPhone(new BookingRequest(1, "Miles Morales")))
                .hasValue(new Booking(1, 1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 0, 0, 0)));
    }

    @Test
    void bookPhone_shouldReturnEmptyIfPhoneDoesNotExist() {
        var repositoryMock = mock(BookingsRepository.class);
        var entity = new BookingEntity(null, new PhoneEntity(1, null, 1), "Miles Morales", LocalDateTime.of(2023, 1, 1, 0, 0, 0));
        when(repositoryMock.save(entity))
                .thenThrow(new TransientPropertyValueException(null, null, null, null));

        var service = new BookingsService(repositoryMock, mock(PhoneRepository.class), fixedClock);
        assertThat(service.bookPhone(new BookingRequest(1, "Miles Morales"))).isEmpty();
    }

    @Test
    public void bookPhone_shouldThrowExceptionIfPhoneAlreadyBooked() {
        var repositoryMock = mock(BookingsRepository.class);
        when(repositoryMock.countByPhoneId(1)).thenReturn(2);

        var phoneRepositoryMock = mock(PhoneRepository.class);
        when(phoneRepositoryMock.findById(1)).thenReturn(Optional.of(new PhoneEntity(1, null, 2)));


        var service = new BookingsService(repositoryMock, phoneRepositoryMock, fixedClock);

        var request = new BookingRequest(1, "Miles Morales");
        assertThrows(BookingsService.PhoneBookedException.class, () -> service.bookPhone(request));
    }
}