package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.HotelDto;
import com.codingsrv.projects.airBnbApp.dto.HotelInfoDto;
import com.codingsrv.projects.airBnbApp.dto.RoomDto;
import com.codingsrv.projects.airBnbApp.dto.RoomPriceDto;
import com.codingsrv.projects.airBnbApp.entity.Hotel;
import com.codingsrv.projects.airBnbApp.entity.Room;
import com.codingsrv.projects.airBnbApp.entity.User;
import com.codingsrv.projects.airBnbApp.exception.ResourceNotFoundException;
import com.codingsrv.projects.airBnbApp.exception.UnAuthorisedException;
import com.codingsrv.projects.airBnbApp.repository.HotelRepository;
import com.codingsrv.projects.airBnbApp.repository.InventoryRepository;
import com.codingsrv.projects.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.codingsrv.projects.airBnbApp.util.AppUtils.getCurrentUser;

@RequiredArgsConstructor
@Slf4j
@Service
public class HotelServiceImpl implements HotelService{
    private final RoomRepository roomRepository;

    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}",hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false); //hardcoding hotel active as false initially,as there is not any inventory for hotel they are just onboarded.

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}",hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("Getting hotel with ID: {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("hotel not found with ID: "+ hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id: "+hotelId);
        }

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long hotelId, HotelDto hotelDto) {
        log.info("updating a hotel with ID: {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("hotel not found with ID: "+hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id: "+hotelId);
        }


        modelMapper.map(hotelDto,hotel);
        hotel.setId(hotelId);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @jakarta.transaction.Transactional
    public void deleteHotelById(Long hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("hotel not found with ID: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id: "+hotelId);
        }


        // Delete inventory and rooms first
        for (Room room : hotel.getRooms()) {
            inventoryService.deleteFutureInventory(room);
            roomRepository.delete(room);
        }
        //  Delete hotel last
        hotelRepository.delete(hotel);
    }


    @Override
    @Transactional  // as two different database call happening inside one method.
    public void activateHotel(Long hotelId) {
        log.info("activating the hotel with ID: {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with this ID: "+hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id: "+hotelId);
        }


        hotel.setActive(true);

        //creating inventory (only one time) for all the rooms for this hotel
        for (Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }

    }
    // public method
    @Override
    public HotelInfoDto findHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        List<RoomDto> rooms = hotel.getRooms()
                .stream()
                .map(element->modelMapper.map(element, RoomDto.class))
                .toList();
        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class),rooms);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        User user = getCurrentUser();
        log.info("Getting all hotels for the admin user with ID: {}", user.getId());

        List<Hotel> hotels = hotelRepository.findAll();

        return hotels
                .stream()
                .map((element) -> modelMapper.map(element, HotelDto.class))
                .collect(Collectors.toList());
    }

}