package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockPerformance {
    private int numDays;
    private int ninetyDayVolAvg;
    private float currentValue;
    private float previousValue;
    private float purchaseValue;
    private float runningGainOrLoss;

    public void addGainOrLoss(float amt) {
        runningGainOrLoss += amt;
    }

}
