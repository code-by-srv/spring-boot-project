package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.HotelBrowseDto;
import com.codingsrv.projects.airBnbApp.dto.HotelPriceDto;
import com.codingsrv.projects.airBnbApp.dto.InventoryDto;
import com.codingsrv.projects.airBnbApp.dto.UpdateInventoryRequestDto;
import com.codingsrv.projects.airBnbApp.entity.Inventory;
import com.codingsrv.projects.airBnbApp.entity.Room;
import com.codingsrv.projects.airBnbApp.entity.User;
import com.codingsrv.projects.airBnbApp.exception.ResourceNotFoundException;
import com.codingsrv.projects.airBnbApp.repository.HotelMinPriceRepository;
import com.codingsrv.projects.airBnbApp.repository.InventoryRepository;
import com.codingsrv.projects.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.codingsrv.projects.airBnbApp.util.AppUtils.getCurrentUser;

@RequiredArgsConstructor
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService{

    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final InventoryRepository inventoryRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;


    @Override
    public void initializeRoomForAYear(Room room) {
        log.info("Initializing room for a year with room: {}",room);
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for (; !today.isAfter(endDate) ; today = today.plusDays(1)){
            Inventory inventory = Inventory.builder()  // building instance of Inventory using all its fields
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);  // now save the inventory
        }
    }



    @Override
    public void deleteFutureInventory(Room room) {
        log.info("Deleting the inventories of room with id: {}", room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelBrowseDto hotelBrowseDto) {
        log.info("Searching hotels for {} city, from {} to {}",hotelBrowseDto.getCity(),hotelBrowseDto.getStartDate(),hotelBrowseDto.getEndDate());
        Pageable pageable = PageRequest.of(hotelBrowseDto.getPage(),hotelBrowseDto.getSize());

        Long dateCount = ChronoUnit.DAYS.between(hotelBrowseDto.getStartDate(), hotelBrowseDto.getEndDate()) + 1;

        // business logic - 90 days
       Page<HotelPriceDto> hotelPage = hotelMinPriceRepository.findHotelWithAvailableInventory(hotelBrowseDto.getCity(),
               hotelBrowseDto.getStartDate(), hotelBrowseDto.getEndDate(),hotelBrowseDto.getRoomsCount(),
               dateCount,pageable);

       return hotelPage;
    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Getting All inventory by room for room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of room with id: "+roomId);

        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element) -> modelMapper.map(element,
                        InventoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating All inventory by room for room with id: {} between date range: {} - {}", roomId,
                updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of room with id: "+roomId);

        // before updating first lock those inventory
        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId, updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate());

        inventoryRepository.updateInventory(roomId, updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate(), updateInventoryRequestDto.getClosed(),
                updateInventoryRequestDto.getSurgeFactor());
    }
}
