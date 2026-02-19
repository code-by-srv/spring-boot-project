package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.HotelBrowseDto;
import com.codingsrv.projects.airBnbApp.dto.HotelDto;
import com.codingsrv.projects.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);  // creating inventory of rooms for the next one year

    void deleteFutureInventory(Room room);

    Page<HotelDto> searchHotels(HotelBrowseDto hotelBrowseDto);
}
