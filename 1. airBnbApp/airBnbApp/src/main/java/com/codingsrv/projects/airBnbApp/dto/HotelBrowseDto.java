package com.codingsrv.projects.airBnbApp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelBrowseDto {

    private String city;
    private LocalDate startDate;   // checkInDate
    private LocalDate endDate;     // checkOutDate
    private Integer roomsCount;

    // For paginated results
    private Integer page = 0;  // default
    private Integer size = 10; // default



}
