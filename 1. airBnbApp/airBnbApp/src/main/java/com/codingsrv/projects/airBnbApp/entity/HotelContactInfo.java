package com.codingsrv.projects.airBnbApp.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable  // we don't want new table but want this table to be embedded in hotel table.
public class HotelContactInfo {
    private String address;
    private String phoneNumber;
    private String email;
    private String location;



}
