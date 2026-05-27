package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.RoomDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);

    List<RoomDto> getAllRoomsInHotel(Long hotelId);

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long roomId);

    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
