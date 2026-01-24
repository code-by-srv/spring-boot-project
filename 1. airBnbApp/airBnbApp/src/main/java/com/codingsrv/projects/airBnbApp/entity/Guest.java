package com.codingsrv.projects.airBnbApp.entity;

import com.codingsrv.projects.airBnbApp.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    private Integer age;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "guests")
    private Set<Booking> bookings;








}
