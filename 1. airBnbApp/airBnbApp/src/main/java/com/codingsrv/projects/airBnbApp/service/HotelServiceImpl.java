package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.HotelDto;
import com.codingsrv.projects.airBnbApp.entity.Hotel;
import com.codingsrv.projects.airBnbApp.entity.Room;
import com.codingsrv.projects.airBnbApp.exception.ResourceNotFoundException;
import com.codingsrv.projects.airBnbApp.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}",hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false); //hardcoding hotel active as false initially,as there is not any inventory for hotel they are just onboarded.
        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}",hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("Getting hotel with ID: {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("hotel not found with ID: "+ hotelId));

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long hotelId, HotelDto hotelDto) {
        log.info("updating a hotel with ID: {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("hotel not found with ID: "+hotelId));
        modelMapper.map(hotelDto,hotel);
        hotel.setId(hotelId);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional // as two different database call happening inside one method.
    public void deleteHotelById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("hotel not found with ID: "+hotelId));
        hotelRepository.deleteById(hotelId);

        // deleting the future inventory for this hotel by id
        for (Room room : hotel.getRooms()){
            inventoryService.deleteFutureInventory(room);
        }

    }

    @Override
    @Transactional  // as two different database call happening inside one method.
    public void activateHotel(Long hotelId) {
        log.info("activating the hotel with ID: {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with this ID: "+hotelId));
        hotel.setActive(true);

       //creating inventory (only one time) for all the rooms for this hotel
        for (Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }

    }

}
