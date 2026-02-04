package com.codingsrv.projects.airBnbApp.advice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private LocalDateTime timeStamp;
    private T data;
    private ApiError error;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();   // timestamp set here
    }

    public ApiResponse(T data) {
        this();        //  calls default constructor
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this();        // calls default constructor
        this.error = error;
    }
}
