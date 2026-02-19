package com.codingsrv.projects.airBnbApp.repository;

import com.codingsrv.projects.airBnbApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}