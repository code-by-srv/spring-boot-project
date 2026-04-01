package com.codingsrv.projects.airBnbApp.dto;

import com.codingsrv.projects.airBnbApp.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HotelPriceDto {

    private Hotel hotel;
    private Double price;

}
