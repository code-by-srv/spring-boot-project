package com.codingsrv.projects.airBnbApp.repository;

import com.codingsrv.projects.airBnbApp.entity.Inventory;
import com.codingsrv.projects.airBnbApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByDateAfterAndRoom(LocalDate date, Room room);
}
