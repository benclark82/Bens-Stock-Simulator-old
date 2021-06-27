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
    private boolean isInvested;
    //1.00 is 100%
    private static final float STARTAMT = 1.00f;
    //In decimal form. 100% would be 1.00
    private float totalGainOrLossAmtDecimal;

    public void runSimulation() {

        Iterator<StockDay> stockDayIterator = stockDays.listIterator();
        Iterator<StockPerformance> stockPerformanceIterator = stockPerformanceList.listIterator();
        StockPerformance stockPerformance;
        float gainOrLossAmt;

        //for each day between startDate and endDate
        for(LocalDate date : dateRange.toList()) {

            //TODO: Should this increment stock performance? (Idea is go through all stocksDays for that date
            //      so we can see what our performance for the day was?)
            //for each stock day/stock performance
            while(stockDayIterator.hasNext() && stockPerformanceIterator.hasNext()) {

                stockPerformance = stockPerformanceIterator.next();

                //if not invested
                if (!isInvested) {

                    //check if we should buy
                    if (stockBuySellStrategy.shouldBuyStock(stockDays, stockPerformance)) {
                        isInvested = true;
                        //set purchase price
                    }
                } else {
                    //check if we should sell
                    if (stockBuySellStrategy.shouldSellStock(stockDays, stockPerformance) > 0) {
                        isInvested = false;

                        //calculate gain or loss amount
                        gainOrLossAmt = calculateGainOrLossAmount(STARTAMT, stockPerformance.getCurrentValue());
                        totalGainOrLossAmtDecimal += gainOrLossAmt;
                        stockPerformance.addGainOrLoss(gainOrLossAmt);

                    }
                }
            }
        }
    }

    private float calculateGainOrLossAmount(float startAmt, float endAmt) {
        float gainOrLossAmt = endAmt - startAmt;

        return gainOrLossAmt;
    }
}
