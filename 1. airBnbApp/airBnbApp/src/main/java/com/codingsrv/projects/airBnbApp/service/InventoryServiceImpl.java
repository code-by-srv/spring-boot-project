package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.dto.HotelBrowseDto;
import com.codingsrv.projects.airBnbApp.dto.HotelPriceDto;
import com.codingsrv.projects.airBnbApp.entity.Inventory;
import com.codingsrv.projects.airBnbApp.entity.Room;
import com.codingsrv.projects.airBnbApp.repository.HotelMinPriceRepository;
import com.codingsrv.projects.airBnbApp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService{

    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final InventoryRepository inventoryRepository;
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
}
