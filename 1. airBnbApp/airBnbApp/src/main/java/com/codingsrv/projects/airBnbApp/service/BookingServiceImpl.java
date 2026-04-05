package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.BookingDto;
import com.codingsrv.projects.airBnbApp.dto.BookingRequestDto;
import com.codingsrv.projects.airBnbApp.dto.GuestDto;
import com.codingsrv.projects.airBnbApp.entity.*;
import com.codingsrv.projects.airBnbApp.entity.enums.BookingStatus;
import com.codingsrv.projects.airBnbApp.exception.ResourceNotFoundException;
import com.codingsrv.projects.airBnbApp.exception.UnAuthorisedException;
import com.codingsrv.projects.airBnbApp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class BookingServiceImpl implements BookingService{
    private final GuestRepository guestRepository;

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequestDto bookingRequestDto) {
        log.info("Initialising booking for hotel : {}, room :{}, date:{}-{}",bookingRequestDto.getHotelId(),
                bookingRequestDto.getRoomId(),bookingRequestDto.getCheckInDate(),bookingRequestDto.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId())
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: "+bookingRequestDto.getHotelId()));

        Room room = roomRepository.findById(bookingRequestDto.getRoomId())
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with id: "+bookingRequestDto.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequestDto.getCheckInDate(),bookingRequestDto.getCheckOutDate(),bookingRequestDto.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(),bookingRequestDto.getCheckOutDate())+1;
        if (inventoryList.size() != daysCount){
            throw new IllegalStateException("Room is not available anymore");
        }

        // reserve the room or update the bookCount of inventory
        for (Inventory inventory : inventoryList){
            inventory.setReservedCount(inventory.getReservedCount()+bookingRequestDto.getRoomsCount());
        }
        inventoryRepository.saveAll(inventoryList);

        // create the booking


        //TODO: Calculate dynamic price

        Booking booking =Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingRequestDto.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public  BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding guest for booking with id: {}",bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found with id: "+bookingId));

        User user =getCurrentUser();

        if (!user.equals(booking.getUser())){
            throw new UnAuthorisedException("Booking does not belongs to this user with id"+user.getId());
        }

        if (hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has already expired");
        }

        if (booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state, cannot state guest");
        }

        for (GuestDto guestDto : guestDtoList){
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(user);
            guest= guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);
    }


    public boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
