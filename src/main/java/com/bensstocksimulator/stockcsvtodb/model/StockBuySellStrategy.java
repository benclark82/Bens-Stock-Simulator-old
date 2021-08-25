package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Data
public class StockBuySellStrategy {

    private float sellPrice;
    private float buyLimitPrice;
    private float stopLossPrice;
    private float sellLimitPrice;
    private boolean buyWhenPreviousWasDown;
    private boolean buyWhenPreviousWasUp;
    private boolean sellWhenPreviousWasDown;
    private boolean sellWhenPreviousWasUp;
    private boolean isBuyIfBullishHarami;
    private boolean isSellIfBearishHarami;
    private int buyWhenPreviousDuration;
    private int sellWhenPreviousDuration;
    private int buyWhenPreviousCounter;
    private int sellWhenPreviousCounter;


    public StockBuySellStrategy(StockSimulationConfiguration simConfig) {
        sellPrice = 0.0f;
        buyLimitPrice = 0.0f;
        stopLossPrice = 0.0f;
        sellLimitPrice = 0.0f;
        buyWhenPreviousWasDown = simConfig.isBuyIfPreviouslyDown();
        buyWhenPreviousWasUp = simConfig.isBuyIfPreviouslyUp();
        sellWhenPreviousWasDown = simConfig.isSellIfPreviouslyDown();
        sellWhenPreviousWasUp = simConfig.isSellIfPreviouslyUp();
        isBuyIfBullishHarami = simConfig.isBuyIfBullishHarami();
        isSellIfBearishHarami = simConfig.isSellIfBearishHarami();
        buyWhenPreviousDuration = simConfig.getPreviousBuyDuration();
        sellWhenPreviousDuration = simConfig.getPreviousSellDuration();
        buyWhenPreviousCounter = 0;
        sellWhenPreviousCounter = 0;

        log.debug("Stock buy sell strategy is {}", this);
    }

    /*
     *   Returns 0 if we didn't buy, or the buy price if we did buy
     */
    public float shouldBuyStock(List<StockDay> stockDays, StockPerformance stockPerformance) {
        StockDay currentStockDay;
        buyWhenPreviousCounter++;

        //If havent' met duration then can't buy
        if (buyWhenPreviousCounter < buyWhenPreviousDuration)
            return 0.0f;

        if (stockDays.size() == 0) {
            log.error("shouldBuyStock: Error - stockDays is empty");
            return 0.0f;
        } else
            currentStockDay = stockDays.get(stockDays.size() - 1);

        //If we have a stock buy price set and it was met
        if (buyLimitPrice != 0.0 && stockPriceMet(buyLimitPrice, currentStockDay)) {
            log.debug("Buy at limit price: {}", buyLimitPrice);
            sellWhenPreviousCounter = 0;
            return buyLimitPrice;
        }

        //If we have enough days for duration & buy stock if previous duration was up/down setting enabled then buy stock
        if (stockDays.size() > buyWhenPreviousDuration &&
                (buyWhenPreviousWasDown && previousDurationStockIsDown(stockDays, buyWhenPreviousDuration)) ||
                (buyWhenPreviousWasUp && !previousDurationStockIsDown(stockDays, buyWhenPreviousDuration))) {
            log.debug("Buy {} for {} on {} because previous duration was up/down",
                    currentStockDay.getTicker(), currentStockDay.getOpen(), currentStockDay.getDate());
            //Buy stock at opening price
            sellWhenPreviousCounter = 0;
            return currentStockDay.getOpen();
        }


        //If we have buy on bullish harami set and we have one
        if (isBuyIfBullishHarami && bullishHaramiFound(stockDays)) {
            sellWhenPreviousCounter = 0;
            return currentStockDay.getClose();
        }

        return 0.0f;
    }

    /**
     * Returns true if the stock was down during the specified duration
     *
     * @param stockDays
     * @param previousBuyDuration
     * @return
     */
    private boolean previousDurationStockIsDown(List<StockDay> stockDays, int previousBuyDuration) {
        float netDurationGain;
        boolean isDown;
        StockDay previousStockDay = stockDays.get(stockDays.size() - 2);
        StockDay previousDurationStockDay;

        previousDurationStockDay = stockDays.get(stockDays.size() - previousBuyDuration - 1);
        // subtract current stock day close from previous duration stockday close
        netDurationGain = previousStockDay.getClose() - previousDurationStockDay.getOpen();
        System.out.println("PreviousDay(close): " + previousStockDay.getClose() + ", previousDuration(open): " + previousDurationStockDay.getOpen());
        isDown = netDurationGain < 0.0;

        return isDown;
    }

    /*
     *    1) Checks current stock day to see if sell price met
     *    2) Checks if previous day down stock sell setting met
     *    Returns 0.0f if we didn't sell, or the sell price if we did buy
     */
    public float shouldSellStock(List<StockDay> stockDays, StockPerformance stockPerformance) {
        StockDay previousStockDay = stockDays.get(stockDays.size() - 2);
        StockDay currentStockDay = stockDays.get(stockDays.size() - 1);

        //If havent' met duration then can't sell
        sellWhenPreviousCounter++;
        if (sellWhenPreviousCounter < sellWhenPreviousDuration)
            return 0.0f;

        //If sell limit price was hit, then sell for that price
        if (stockPriceMet(sellLimitPrice, currentStockDay)) {
            log.debug("Sell limit price of {} was met on {}", sellLimitPrice, currentStockDay.getDate());
            buyWhenPreviousCounter = 0;
            return sellLimitPrice;
        }

        //Sell at current day open price if setting is to sell when previous day down
        if (sellWhenPreviousWasDown &&
                previousStockDay.getClose() < previousStockDay.getOpen()) {
            log.debug("Sell at current day (when previous day down) open price: {} on {}", currentStockDay.getOpen(), currentStockDay.getDate());
            buyWhenPreviousCounter = 0;
            return currentStockDay.getOpen();
        }

        //Sell at current day open price if setting is to sell when previous day up
        if (sellWhenPreviousWasUp &&
                previousStockDay.getClose() > previousStockDay.getOpen()) {
            log.debug("Sell at current day (when previous day up) open price: {} on {}", currentStockDay.getOpen(), currentStockDay.getDate());
            buyWhenPreviousCounter = 0;
            return currentStockDay.getOpen();
        }

        //If we have buy on bullish harami set and we have one
        if (isSellIfBearishHarami && bearishHaramiFound(stockDays)) {
            buyWhenPreviousCounter = 0;
            return currentStockDay.getClose();
        }

        return 0.0f;
    }

    //Returns true if stock price was met between stock day low and stock day high
    public boolean stockPriceMet(float stockPrice, StockDay stockDay) {
        if (stockPrice <= stockDay.getHigh() && stockPrice >= stockDay.getLow()) {
            log.debug("Stock price of {} met on {}", stockPrice, stockDay.getDate());
            return true;
        }

        return false;
    }

    /**
     * Returns true if bullish harami found at end of stockDays.  Summary of bullish harami:
     * 1) Downward trend of 2 or more days
     * 2) Bullish candle is 25% of previous days bearish candle AND higher than previous day close
     *
     * @param stockDays
     * @return
     */
    public boolean bullishHaramiFound(List<StockDay> stockDays) {
        StockDay twoDaysBeforeStockDay = stockDays.get(stockDays.size() - 3);
        StockDay previousStockDay = stockDays.get(stockDays.size() - 2);
        StockDay currentStockDay = stockDays.get(stockDays.size() - 1);

        //if stock is on a downward trend(2 or more days)
        if (previousStockDay.getClose() < twoDaysBeforeStockDay.getClose()) {

            //if bullish candle is 25% of last bearish trending candle AND open is higher than previous day close
            if (previousStockDay.getOpenCloseRangeAmt() / currentStockDay.getOpenCloseRangeAmt() >= 4 &&
                    currentStockDay.getOpen() > previousStockDay.getClose()) {
                log.debug("Bullish Harami found on {}", currentStockDay.getDate());
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if bearish harami found at end of stockDays.  Summary of bearish harami:
     * 1) Upward trend of 2 or more days before last day
     * 2) Last candle is 25% of previous days bullish candle AND lower than previous day close
     *
     * @param stockDays
     * @return
     */
    public boolean bearishHaramiFound(List<StockDay> stockDays) {
        StockDay twoDaysBeforeStockDay = stockDays.get(stockDays.size() - 3);
        StockDay previousStockDay = stockDays.get(stockDays.size() - 2);
        StockDay currentStockDay = stockDays.get(stockDays.size() - 1);

        //if stock is on an upward trend(2 or more days)
        if (previousStockDay.getClose() > twoDaysBeforeStockDay.getClose()) {

            //if bullish candle is 25% of last bullish trending candle AND open is lower than previous day close
            if (previousStockDay.getOpenCloseRangeAmt() / currentStockDay.getOpenCloseRangeAmt() >= 4 &&
                    currentStockDay.getOpen() < previousStockDay.getClose()) {
                log.debug("Bearish Harami found on {}", currentStockDay.getDate());
                return true;
            }
        }
        return false;
    }
}
