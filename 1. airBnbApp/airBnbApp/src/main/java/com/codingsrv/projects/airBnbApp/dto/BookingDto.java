package com.codingsrv.projects.airBnbApp.dto;


import com.codingsrv.projects.airBnbApp.entity.Hotel;
import com.codingsrv.projects.airBnbApp.entity.Room;
import com.codingsrv.projects.airBnbApp.entity.User;
import com.codingsrv.projects.airBnbApp.entity.enums.BookingStatus;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class BookingDto {

    private Long id;

    private Hotel hotel;


    private Room room;

    private User user;

    private Integer roomsCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String createdAt;

    private String updatedAt;

    private BookingStatus bookingStatus;

    private Set<GuestDto> guests;



}
