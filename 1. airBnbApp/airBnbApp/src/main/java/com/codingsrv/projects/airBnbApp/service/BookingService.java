package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.BookingDto;
import com.codingsrv.projects.airBnbApp.dto.BookingRequestDto;
import com.codingsrv.projects.airBnbApp.dto.GuestDto;
import com.codingsrv.projects.airBnbApp.dto.HotelReportDto;
import com.codingsrv.projects.airBnbApp.entity.enums.BookingStatus;
import com.stripe.model.Event;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {


   BookingDto initialiseBooking(BookingRequestDto bookingRequestDto);

     BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);


    BookingStatus getBookingStatus(Long bookingId);

     List<BookingDto> getAllBookingsByHotelId(Long hotelId);

     HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

     List<BookingDto> getMyBookings();
}
