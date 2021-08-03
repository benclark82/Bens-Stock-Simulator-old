package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Data
@Component
public class StockBuySellStrategy {

    private StockSimulationConfiguration stockSimConfig;

    private float stockSellPrice;
    private float stockBuyPrice;
    private float stopLossPrice;
    private float sellLimitPrice;

    public StockBuySellStrategy(StockSimulationConfiguration stockSimConfig, float stockSellPrice,
                                float stockBuyPrice, float stopLossPrice, float sellLimitPrice) {
        this.stockSimConfig = stockSimConfig;
        this.stockSellPrice = stockSellPrice;
        this.stockBuyPrice = stockBuyPrice;
        this.stopLossPrice = stopLossPrice;
        this.sellLimitPrice = sellLimitPrice;
    }

    /*
     *   Returns 0 if we didn't buy, or the buy price if we did buy
     */
    public float shouldBuyStock(List<StockDay> stockDays, StockPerformance stockPerformance) {
        StockDay currentStockDay;

        if(stockDays.size() == 0) {
            System.out.println("shouldBuyStock: Error - stockDays is empty");
            return 0.0f;
        } else
            currentStockDay = stockDays.get(stockDays.size()-1);

        //If we have a stock buy price set and it was met
        if(stockBuyPrice != 0.0 && stockPriceMet(stockBuyPrice, currentStockDay))
            return stockBuyPrice;

        //If we have buy on bullish harami set and we have one
        if(stockSimConfig.isBuyIfBullishHarami() && bullishHaramiFound(stockDays))
            return currentStockDay.getClose();

        return 0.0f;
    }

    /*
     *    1) Checks current stock day to see if sell price met
     *      2) Checks if previous day down stock sell setting met
     *    Returns 0.0f if we didn't sell, or the sell price if we did buy
     */
    public float shouldSellStock(List<StockDay> stockDays, StockPerformance stockPerformance) {
        StockDay previousStockDay = stockDays.get(stockDays.size()-2);
        StockDay currentStockDay = stockDays.get(stockDays.size()-1);

        //If sell limit price was hit, then sell for that price
        if(stockPriceMet(sellLimitPrice, currentStockDay))
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
        if(stockPrice <= stockDay.getHigh() && stockPrice >= stockDay.getLow())
            return true;

        return false;
    }

    /**
     * Returns true if bullish harami found at end of stockDays.  Summary of bullish harami:
     *      1) Downward trend of 2 or more days
     *      2) Bullish candle is 25% of previous days bearish candle AND higher than previous day close
     * @param stockDays
     * @return
     */
    public boolean bullishHaramiFound(List<StockDay> stockDays) {
        StockDay twoDaysBeforeStockDay = stockDays.get(stockDays.size()-3);
        StockDay previousStockDay = stockDays.get(stockDays.size()-2);
        StockDay currentStockDay = stockDays.get(stockDays.size()-1);

        //if stock is on a downward trend(2 or more days)
        if(previousStockDay.getClose() < twoDaysBeforeStockDay.getClose()) {

            //if bullish candle is 25% of last bearish trending candle AND open is higher than previous day close
            if(previousStockDay.getOpenCloseRangeAmt() / currentStockDay.getOpenCloseRangeAmt() >= 4 &&
                    currentStockDay.getOpen() > previousStockDay.getClose()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if bearish harami found at end of stockDays.  Summary of bearish harami:
     *      1) Upward trend of 2 or more days before last day
     *      2) Last candle is 25% of previous days bullish candle AND lower than previous day close
     * @param stockDays
     * @return
     */
    public boolean bearishHaramiFound(List<StockDay> stockDays) {
        StockDay twoDaysBeforeStockDay = stockDays.get(stockDays.size()-3);
        StockDay previousStockDay = stockDays.get(stockDays.size()-2);
        StockDay currentStockDay = stockDays.get(stockDays.size()-1);

        //if stock is on an upward trend(2 or more days)
        if(previousStockDay.getClose() > twoDaysBeforeStockDay.getClose()) {

            //if bullish candle is 25% of last bullish trending candle AND open is lower than previous day close
            if(previousStockDay.getOpenCloseRangeAmt() / currentStockDay.getOpenCloseRangeAmt() >= 4 &&
                currentStockDay.getOpen() < previousStockDay.getClose()) {
                return true;
            }
        }
        return false;
    }
}
