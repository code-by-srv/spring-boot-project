package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.entity.Inventory;
import com.codingsrv.projects.airBnbApp.entity.Room;
import com.codingsrv.projects.airBnbApp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;


    @Override
    public void initializeRoomForAYear(Room room) {
        log.info("Initializing room for a year with room: {}",room);
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for (; !today.isAfter(endDate) ; today = today.plusDays(1)){
            Inventory inventory = Inventory.builder()  // building instance of Inventory using all its fields
                    .hotel(room.getHotel())
                    .room(room)
                    .bookCount(0)
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
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByDateAfterAndRoom(today,room);

    }
}
