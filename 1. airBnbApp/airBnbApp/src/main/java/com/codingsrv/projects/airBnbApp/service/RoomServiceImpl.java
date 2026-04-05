package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.RoomDto;
import com.codingsrv.projects.airBnbApp.entity.Hotel;
import com.codingsrv.projects.airBnbApp.entity.Room;
import com.codingsrv.projects.airBnbApp.entity.User;
import com.codingsrv.projects.airBnbApp.exception.ResourceNotFoundException;
import com.codingsrv.projects.airBnbApp.exception.UnAuthorisedException;
import com.codingsrv.projects.airBnbApp.repository.HotelRepository;
import com.codingsrv.projects.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;


    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("creating a room in hotel with ID: {} ",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)   // first check whether hotel exist with this id.
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id: "+hotelId);
        }


        Room room = modelMapper.map(roomDto,Room.class);
        room.setHotel(hotel);

        room = roomRepository.save(room);

        //creating inventory as soon as room is created for one year (if hotel is active)
        if (hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel with ID: {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with this ID: "+ hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id: "+hotelId);
        }


        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting a room with id: {}",roomId);
       Room room = roomRepository.findById(roomId)
               .orElseThrow(()-> new ResourceNotFoundException("Room not found with ID: "+roomId));

       return modelMapper.map(room, RoomDto.class);

    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with this ID: "+roomId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorisedException("This user does not own the room with id: "+roomId);
        }


        //deleting all future inventory for this room
        inventoryService.deleteFutureInventory(room);

        roomRepository.deleteById(roomId);  // first delete inventory then only delete that room from DB
    }
}
