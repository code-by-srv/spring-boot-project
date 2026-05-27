package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.*;
import com.codingsrv.projects.airBnbApp.entity.Room;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    void initializeRoomForAYear(Room room);  // creating inventory of rooms for the next one year

    void deleteFutureInventory(Room room);

    Page<HotelPriceDto> searchHotels(HotelBrowseDto hotelBrowseDto);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
