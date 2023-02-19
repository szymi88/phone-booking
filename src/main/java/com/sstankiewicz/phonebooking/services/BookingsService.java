package com.sstankiewicz.phonebooking.services;

import com.sstankiewicz.phonebooking.model.Booking;
import com.sstankiewicz.phonebooking.model.BookingRequest;
import com.sstankiewicz.phonebooking.repository.BookingEntity;
import com.sstankiewicz.phonebooking.repository.BookingsRepository;
import com.sstankiewicz.phonebooking.repository.PhoneEntity;
import com.sstankiewicz.phonebooking.repository.PhoneRepository;
import org.hibernate.TransientPropertyValueException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
        return bookingsRepository.findByPhoneId(id).stream().map(BookingsService::mapToBooking).toList();
    }

    public void removeBooking(int bookingId) {
        bookingsRepository.deleteById(bookingId);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Optional<Booking> bookPhone(BookingRequest booking) {
        try {
            Optional<PhoneEntity> phone = phoneRepository.findById(booking.phoneId());
            if (phone.isEmpty()) {
                return Optional.empty();
            }

            var stockCount = phone.get().getStockCount();
            var bookedCount = bookingsRepository.countByPhoneId(booking.phoneId());
            if (stockCount <= bookedCount) {
                throw new PhoneBookedException();
            }

            var newEntity = new BookingEntity(null, new PhoneEntity(booking.phoneId(), null, null, 0), booking.clientName(), LocalDateTime.now(clock));

            return Optional.of(mapToBooking(bookingsRepository.save(newEntity)));
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
