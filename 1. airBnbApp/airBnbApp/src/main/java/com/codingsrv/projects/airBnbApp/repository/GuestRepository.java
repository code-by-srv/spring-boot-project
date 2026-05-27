package com.codingsrv.projects.airBnbApp.repository;

import com.codingsrv.projects.airBnbApp.entity.Guest;
import com.codingsrv.projects.airBnbApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByUser(User user);
}