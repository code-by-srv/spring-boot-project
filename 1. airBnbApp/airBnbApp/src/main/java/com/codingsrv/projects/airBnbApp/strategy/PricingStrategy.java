package com.codingsrv.projects.airBnbApp.strategy;



import com.codingsrv.projects.airBnbApp.entity.Inventory;

import java.math.BigDecimal;
public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
