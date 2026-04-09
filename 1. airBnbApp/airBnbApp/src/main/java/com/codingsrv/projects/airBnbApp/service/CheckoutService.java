package com.codingsrv.projects.airBnbApp.service;

import com.codingsrv.projects.airBnbApp.entity.Booking;


public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);


}
