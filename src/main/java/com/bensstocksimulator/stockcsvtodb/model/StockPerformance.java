package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;

@Data
public class StockPerformance {

    boolean isInvested;
    private int numDays;
    private int curNumShares;
    private float ninetyDayVolAvg;
    private float runningGainOrLoss;
    private float boughtPrice;
    private float soldPrice;
    private float totalAmtSharesBought;
    private float totalAmtSharesSold;

    public StockPerformance() {
        isInvested = false;
        numDays = 0;
        curNumShares = 0;
        ninetyDayVolAvg = 0.0f;
        runningGainOrLoss = 0.0f;
        boughtPrice = 0.0f;
        soldPrice = 0.0f;
        totalAmtSharesBought = 0.0f;
        totalAmtSharesSold = 0.0f;

    }

    public void stockBought(float boughtPrice, int numShares) {
        setBoughtPrice(boughtPrice);
        setSoldPrice(0.0f);
        setInvested(true);
        setTotalAmtSharesBought(getTotalAmtSharesBought() + numShares);
        setCurNumShares(numShares);
    }

    public void stockSold(float soldPrice) {
        setTotalAmtSharesSold(getTotalAmtSharesSold()+getCurNumShares());
        setRunningGainOrLoss(getRunningGainOrLoss() + ((soldPrice - getBoughtPrice()) * getCurNumShares()));
        System.out.println("runningGainOrLoss: " + getRunningGainOrLoss());
        setBoughtPrice(0.0f);
        setSoldPrice(soldPrice);
        setCurNumShares(0);
        setInvested(false);
    }


}
