package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.entity.Room;

public interface InventoryService {

    void initializeRoomForAYear(Room room);  // creating inventory of rooms for the next one year

    void deleteFutureInventory(Room room);
}
