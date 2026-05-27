package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.HotelDto;
import com.codingsrv.projects.airBnbApp.dto.HotelInfoDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long hotelId, HotelDto hotelDto);

    void deleteHotelById(Long hotelId);

    void activateHotel(Long hotelId);


    HotelInfoDto findHotelInfoById(Long hotelId);

     List<HotelDto> getAllHotels();
}
