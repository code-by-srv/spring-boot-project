package com.codingsrv.projects.airBnbApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @Column(columnDefinition = "TEXT[]")  // will store url of images but actual image will store with third party.
    private String[] photos;

    @Column(columnDefinition = "TEXT[]")  // wifi, pools etc.
    private String[] amenities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Embedded
    private HotelContactInfo hotelContactInfo; // now HotelContactInfo is part of hotel only.

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<Room> rooms;









}
