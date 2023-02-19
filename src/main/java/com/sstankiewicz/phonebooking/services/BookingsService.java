package com.sstankiewicz.phonebooking.services;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.BookingRequest;
import com.sstankiewicz.phonebooking.repository.BookingEntity;
import com.sstankiewicz.phonebooking.repository.BookingsRepository;
import com.sstankiewicz.phonebooking.repository.PhoneEntity;
import com.sstankiewicz.phonebooking.repository.PhoneRepository;
import jakarta.transaction.Transactional;
import org.hibernate.TransientPropertyValueException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableTransactionManagement
public class BookingsService {

    private final BookingsRepository bookingsRepository;
    private final PhoneRepository phoneRepository;
    private final Clock clock;

    public BookingsService(BookingsRepository bookingsRepository, PhoneRepository phoneRepository, Clock clock) {
        this.clock = clock;
        this.bookingsRepository = bookingsRepository;
        this.phoneRepository = phoneRepository;
    }

    public Optional<Booking> getBooking(int id) {
        return bookingsRepository.findById(id).map(BookingsService::mapToBooking);
    }

    public List<Booking> getBookingsByPhoneId(int id) {
        return List.of(new Booking(id, 1, "Miles Morales", LocalDateTime.of(2023, 1, 1, 12, 0, 0)));
    }

    public boolean removeBooking(long bookingId) {
        return false;
    }

    @Transactional
    public Optional<Booking> bookPhone(BookingRequest booking) {
        try {
            Optional<PhoneEntity> phone = phoneRepository.findById(booking.phoneId());
            if (phone.isEmpty()) {
                return Optional.empty();
            }

            var stockCount = phone.get().getStockCount();
            var bookedCount = bookingsRepository.countByPhoneId(booking.phoneId());
            System.out.println(bookedCount+"/"+stockCount);
            System.out.println(bookingsRepository.findAll());
            if (stockCount <= bookedCount) {
                throw new PhoneBookedException();
            }

            var entity = bookingsRepository.save(new BookingEntity(null, new PhoneEntity(booking.phoneId(), null, 0), booking.clientName(), LocalDateTime.now(clock)));

            return Optional.of(mapToBooking(entity));
        } catch (TransientPropertyValueException e) {
            return Optional.empty();
        }
    }

    private static Booking mapToBooking(BookingEntity entity) {
        return new Booking(entity.getId(), entity.getPhone().getId(), entity.getBookedBy(), entity.getBookedFrom());
    }

    public static class PhoneBookedException extends RuntimeException {
    }
}
