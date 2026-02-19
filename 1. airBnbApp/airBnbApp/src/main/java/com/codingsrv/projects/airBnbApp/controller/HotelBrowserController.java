package com.codingsrv.projects.airBnbApp.controller;

import com.codingsrv.projects.airBnbApp.dto.HotelBrowseDto;
import com.codingsrv.projects.airBnbApp.dto.HotelDto;
import com.codingsrv.projects.airBnbApp.dto.HotelInfoDto;
import com.codingsrv.projects.airBnbApp.service.HotelService;
import com.codingsrv.projects.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/hotels")
@RestController
public class HotelBrowserController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelBrowseDto hotelBrowseDto){
        Page<HotelDto> page = inventoryService.searchHotels(hotelBrowseDto);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.findHotelInfoById(hotelId));
    }




}
