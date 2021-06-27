package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Data
@Component
public class StockBuySellStrategy {

    @Autowired
    private StockSimulationConfiguration stockSimConfig;
    private float stockSellPrice;
    private float stockBuyPrice;
    private float stopLossPrice;
    private float sellLimitPrice;

    public boolean shouldBuyStock(List<StockDay> stockDays, StockPerformance stockPerformance) {
        StockDay currentStockDay = stockDays.get(stockDays.size()-1);

        //If we have a stock buy price set and it was met
        if(stockBuyPrice != 0.0 && stockPriceMet(stockBuyPrice, currentStockDay))
            return true;

        //If we have buy on bullish harami set and we have one
        if(stockSimConfig.isBuyIfBullishHarami() && bullishHaramiFound(stockDays, stockPerformance))
            return true;

        return false;
    }

    public float shouldSellStock(List<StockDay> stockDays, StockPerformance stockPerformance) {
        StockDay previousStockDay = stockDays.get(stockDays.size()-2);
        StockDay currentStockDay = stockDays.get(stockDays.size()-1);

        //If sell limit price was hit, then sell for that price
        if(sellLimitPrice < currentStockDay.getHigh() &&
            sellLimitPrice > currentStockDay.getLow())
            return sellLimitPrice;

        //Sell at current day open price if setting is to sell when previous day down
        if(getStockSimConfig().isSellIfPreviousDayDown() &&
                previousStockDay.getClose() < previousStockDay.getOpen()) {
            return currentStockDay.getOpen();
        }

        //Sell at current day open price if setting is to sell when previous day up
        if(getStockSimConfig().isSellIfPreviousDayUp() &&
                previousStockDay.getClose() < previousStockDay.getOpen()) {
            return currentStockDay.getOpen();
        }

        //
        return 0.0f;
    }

    //Returns true if stock price was met between stock day low and stock day high
    public boolean stockPriceMet(float stockPrice, StockDay stockDay) {
        if(stockPrice < stockDay.getHigh() && stockPrice > stockDay.getLow())
            return true;

        return false;
    }

    boolean bullishHaramiFound(List<StockDay> stockDays, StockPerformance stockPerformance) {
        //TODO: finish this
        return false;
    }
}
