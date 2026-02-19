package com.codingsrv.projects.airBnbApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class HotelInfoDto {

    private HotelDto hotel;
    private List<RoomDto> rooms;
}
