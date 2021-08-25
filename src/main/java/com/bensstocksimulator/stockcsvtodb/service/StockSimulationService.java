package com.bensstocksimulator.stockcsvtodb.service;

import com.bensstocksimulator.stockcsvtodb.model.*;
import com.bensstocksimulator.stockcsvtodb.repository.StockDayRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Data
@Service
@Slf4j
public class StockSimulationService {

    private static final int DEFAULT_NUM_BUY_SHARES = 100;
    private Map<String, ArrayList<StockDay>> stockDaysMap;
    private final StockDayRepository repository;
    //In decimal form. 100% would be 1.00
    private float totalGainOrLossAmtDecimal;

    public StockSimulationService(StockDayRepository repository) {
        this.repository = repository;
        this.totalGainOrLossAmtDecimal = 0.0f;
    }


    /**
     * Iterates through each day in the date range specified, then iterates through each stock specified
     * and runs strategy to see if we should buy or sell
     */
    public ArrayList<StockPerformance> runSimulation(StockSimulationConfiguration config) {
        ArrayList<StockDay> stockDays;
        ArrayList<StockDay> upToDateStockDays = new ArrayList<>();
        ArrayList<StockPerformance> stockPerformances = new ArrayList<>();
        Iterator<StockDay> stockDayIterator;
        StockPerformance stockPerformance;
        StockDay currentStockDay;
        StockBuySellStrategy strategy = new StockBuySellStrategy(config);
        DateRange dateRange = new DateRange(LocalDate.parse(config.getStartDate()), LocalDate.parse(config.getEndDate()));
        this.stockDaysMap = getStockDays(config.getStockTickers(), dateRange);
        float shouldBuyStockValue;
        float shouldSellStockValue;

        log.debug("Running simulation between: {} and {}", config.getStartDate(), config.getEndDate());

        //for each stock ticker and all the stock days for it in between date range
        for (Map.Entry<String, ArrayList<StockDay>> tickerEntry : stockDaysMap.entrySet()) {
            stockDays = tickerEntry.getValue();
            stockDayIterator = stockDays.listIterator();
            stockPerformance = new StockPerformance();

            log.debug("Running simulation for stock: {}", tickerEntry.getKey());
            while (stockDayIterator.hasNext()) {
                currentStockDay = stockDayIterator.next();
                upToDateStockDays.add(currentStockDay);
                stockPerformance.setNumDays(stockPerformance.getNumDays() + 1);
                if (!stockPerformance.isInvested()) {
                    // will return 0.0 if we don't buy, > 0.0 if we do buy
                    shouldBuyStockValue = runNotInvestedStrategy(upToDateStockDays, stockPerformance, strategy);

                    if (shouldBuyStockValue > 0.0f)
                        stockPerformance.stockBought(shouldBuyStockValue, DEFAULT_NUM_BUY_SHARES);
                } else {
                    // will return 0.0 if we don't sell, > 0.0 if we do sell
                    shouldSellStockValue = runAlreadyInvestedStrategy(upToDateStockDays, stockPerformance, strategy);

                    if (shouldSellStockValue > 0.0f)
                        stockPerformance.stockSold(shouldSellStockValue);
                }
            }
            stockPerformances.add(stockPerformance);
            log.debug("Performance of {} was: {}", tickerEntry.getKey(), stockPerformance);
        }

        return stockPerformances;
    }

    private float runNotInvestedStrategy(List<StockDay> stockDays, StockPerformance stockPerformance,
                                         StockBuySellStrategy strategy) {
        //check if we should buy
        return strategy.shouldBuyStock(stockDays, stockPerformance);
    }

    private float runAlreadyInvestedStrategy(List<StockDay> stockDays, StockPerformance stockPerformance,
                                             StockBuySellStrategy strategy) {
        //check if we should sell
        return strategy.shouldSellStock(stockDays, stockPerformance);
    }

    private Map<String, ArrayList<StockDay>> getStockDays(ArrayList<String> stockTickers, DateRange dateRange) {
        Map<String, ArrayList<StockDay>> stockDaysMap = new HashMap<>();
        ArrayList<StockDay> stockDayList;

        for (String stockTicker : stockTickers) {
            stockDayList = repository.findByTickerAndDateBetween(stockTicker, dateRange.getStartDate(), dateRange.getEndDate());
            stockDaysMap.put(stockTicker, stockDayList);
        }

        return stockDaysMap;
    }
}

