package com.codingsrv.projects.airBnbApp.dto;

import com.codingsrv.projects.airBnbApp.entity.User;
import com.codingsrv.projects.airBnbApp.entity.enums.Gender;

import lombok.Data;

import java.util.Set;

@Data
public class GuestDto {

    private Long id;

    private String name;

    private Gender gender;

    private Integer age;

    private User user;

    private Set<BookingDto> bookings;
}
