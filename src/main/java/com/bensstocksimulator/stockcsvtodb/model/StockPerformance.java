package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockPerformance {

    boolean isInvested;
    private int numDays;
    private int curNumShares;
    private float ninetyDayVolAvg;
    private float purchaseValue;
    private float runningGainOrLoss;
    private float buyPrice;
    private float sellPrice;
    private float totalAmtBought;
    private float totalAmtSold;

    public StockPerformance() {
        isInvested = false;
        numDays = 0;
        curNumShares = 0;
        ninetyDayVolAvg = 0.0f;
        purchaseValue = 0.0f;
        runningGainOrLoss = 0.0f;
        buyPrice = 0.0f;
        sellPrice = 0.0f;
        totalAmtBought = 0.0f;
        totalAmtSold = 0.0f;

    }

    public void stockBought(float buyPrice) {
        this.buyPrice = buyPrice;
        totalAmtBought += curNumShares * buyPrice;

        isInvested = true;
        sellPrice = 0.0f;

    }

    public void stockSold(float sellPrice) {
        this.sellPrice = sellPrice;
        totalAmtSold += curNumShares * sellPrice;

        isInvested = false;
        runningGainOrLoss += curNumShares * (sellPrice-buyPrice);
        buyPrice = 0.0f;

    }

}
