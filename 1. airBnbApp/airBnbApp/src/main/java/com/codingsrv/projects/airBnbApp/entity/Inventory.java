package com.codingsrv.projects.airBnbApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(       // creating a unique key using hotel_id, room_id and date.
        name = "unique_hotel_room_date",
        columnNames = {"hotel_id","room_id","date"}
        ))
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)  // a hotel can have multiple inventory.
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")  // defining default value 0 at beginning.
    private Integer bookCount;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false,precision = 2, scale = 2)  // we can increase price up to 100000 times.
    private BigDecimal surgeFactor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;    // price = basePrice * surgeFactor


    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Boolean closed;


    @UpdateTimestamp
    private String updatedAt;

    @CreationTimestamp
    private String createdAt;












}
