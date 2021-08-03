package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;


@Data
public class StockSimulation {
    private DateRange dateRange;
    @Autowired
    private StockBuySellStrategy stockBuySellStrategy;
    private List<StockDay> stockDays;
    private List<StockPerformance> stockPerformanceList;
    private LocalDate startDate;
    private LocalDate endDate;

    //In decimal form. 100% would be 1.00
    private float totalGainOrLossAmtDecimal;

    /**
     * Iterates through each day in the date range specified, then iterates through each stock specified
     * and runes strategy to see if we should buy or sell
     */
    public void runSimulation() {

        Iterator<StockDay> stockDayIterator = stockDays.listIterator();
        Iterator<StockPerformance> stockPerformanceIterator = stockPerformanceList.listIterator();
        StockPerformance stockPerformance;
        float shouldBuyStockValue = 0.0f;
        float shouldSellStockValue = 0.0f;

        //for each day between startDate and endDate
        for(LocalDate date : dateRange.toList()) {

            //for each stock day/stock performance
            while(stockDayIterator.hasNext()) {

                stockPerformance = stockPerformanceIterator.next();

                //if not invested
                if (!stockPerformance.isInvested()) {
                    // will return 0.0 if we don't buy, > 0.0 if we do buy
                    shouldBuyStockValue = runNotInvestedStrategy(shouldBuyStockValue, stockPerformance);

                } else {
                    // will return 0.0 if we don't sell, > 0.0 if we do sell
                    shouldSellStockValue = runAlreadyInvestedStrategy(shouldSellStockValue, stockPerformance);
                }
            }
        }
    }

    private float calculateGainOrLossAmount(float startAmt, float endAmt) {
        float gainOrLossAmt = endAmt - startAmt;

        return gainOrLossAmt;
    }

    private float runNotInvestedStrategy(float shouldBuyStockValue, StockPerformance stockPerformance) {
        //check if we should buy
        shouldBuyStockValue = stockBuySellStrategy.shouldBuyStock(stockDays, stockPerformance);
        if(shouldBuyStockValue > 0.0f) {

            stockPerformance.stockBought( shouldBuyStockValue );
        }

        return shouldBuyStockValue;
    }

    private float runAlreadyInvestedStrategy(float shouldSellStockValue, StockPerformance stockPerformance) {
        //check if we should sell
        shouldSellStockValue = stockBuySellStrategy.shouldSellStock(stockDays, stockPerformance);
        if(shouldSellStockValue > 0.0f) {

            stockPerformance.stockSold( shouldSellStockValue );
        }

        return shouldSellStockValue;
    }
}

