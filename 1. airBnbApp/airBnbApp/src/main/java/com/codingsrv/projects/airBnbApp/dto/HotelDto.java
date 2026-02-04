package com.codingsrv.projects.airBnbApp.dto;

import com.codingsrv.projects.airBnbApp.entity.HotelContactInfo;
import com.codingsrv.projects.airBnbApp.entity.Room;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HotelDto {

    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private HotelContactInfo hotelContactInfo;
    private Boolean active;
    private List<Room> rooms;



}
