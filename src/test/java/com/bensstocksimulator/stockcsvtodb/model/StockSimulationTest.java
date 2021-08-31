package com.bensstocksimulator.stockcsvtodb.model;

import com.bensstocksimulator.stockcsvtodb.repository.StockDayRepository;
import com.bensstocksimulator.stockcsvtodb.service.StockSimulationService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
class StockSimulationTest {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    StockSimulationConfiguration config;

    @Mock
    StockSimulationService service;

    @Mock
    StockDayRepository repository;

    @Mock
    ArrayList<StockDay> stockDaysList;

    @Mock
    ArrayList<StockPerformance> performanceList;

    @Mock
    StockPerformance performance;

    @Mock
    StockBuySellStrategy strategy;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    void runSimulation_BuyIfPreviouslyUp_SellIfPreviouslyDown() {
        config = new StockSimulationConfiguration();
        stockDaysList = new ArrayList<>();
        repository = Mockito.mock(StockDayRepository.class);
        service = new StockSimulationService(repository);

        config.setBuyIfPreviouslyDown(true);
        config.setSellIfPreviouslyUp(true);
        config.setStartDate("2021-05-20");
        config.setEndDate("2021-05-27");
        config.setPreviousBuyDuration(1);
        config.setPreviousSellDuration(1);
        config.setStockTickers(new ArrayList<>(Arrays.asList("TEST")));

        strategy = new StockBuySellStrategy(config);
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 20),2.00f, 1.00f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 21),1.00f, 2.00f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 22),2.00f, 3.00f, 3.00f, 0.5f, 3.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 23),3.00f, 2.00f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 24),2.00f, 3.00f, 3.00f, 0.5f, 3.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 25),3.00f, 2.00f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 26),2.00f, 1.00f, 2.00f, 0.5f, 1.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 27),1.00f, 2.00f, 3.00f, 0.5f, 2.00f, 500));
        Mockito.when(repository.findByTickerAndDateBetween("TEST", LocalDate.of(2021, 5, 20), LocalDate.of(2021, 05, 27))).thenReturn(stockDaysList);

        performanceList = service.runSimulation(config);
        performance = performanceList.get(0);

        assertEquals(200.0f, performance.getRunningGainOrLoss());
        assertEquals(100, performance.getCurNumShares());
    }

    @Test
    void runSimulation_DurationTwoDays_BuyIfPreviouslyUp_SellIfPreviouslyDown() {
        config = new StockSimulationConfiguration();
        stockDaysList = new ArrayList<>();
        repository = Mockito.mock(StockDayRepository.class);
        service = new StockSimulationService(repository);

        config.setBuyIfPreviouslyDown(true);
        config.setSellIfPreviouslyUp(true);
        config.setStartDate("2021-05-20");
        config.setEndDate("2021-05-27");
        config.setPreviousBuyDuration(2);
        config.setPreviousSellDuration(2);
        config.setStockTickers(new ArrayList<>(Arrays.asList("TEST")));

        strategy = new StockBuySellStrategy(config);
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 20),2.00f, 1.00f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 21),1.00f, 2.00f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 22),2.00f, 1.00f, 3.00f, 0.5f, 3.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 23),2.00f, 1.00f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 24),2.00f, 3.00f, 3.00f, 0.5f, 3.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 25),3.00f, 3.00f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 26),2.00f, 4.00f, 2.00f, 0.5f, 1.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 27),1.00f, 2.00f, 3.00f, 0.5f, 2.00f, 500));
        Mockito.when(repository.findByTickerAndDateBetween("TEST", LocalDate.of(2021, 5, 20), LocalDate.of(2021, 05, 27))).thenReturn(stockDaysList);

        performanceList = service.runSimulation(config);
        performance = performanceList.get(0);

        assertEquals(-100.0f, performance.getRunningGainOrLoss());
        assertEquals(0, performance.getCurNumShares());

    }

    @Test
    void runSimulation_BuyIfBullishHarami_SellIfBearishHarami() {
        config = new StockSimulationConfiguration();
        stockDaysList = new ArrayList<>();
        repository = Mockito.mock(StockDayRepository.class);
        service = new StockSimulationService(repository);

        config.setBuyIfBullishHarami(true);
        config.setSellIfBearishHarami(true);
        config.setStartDate("2021-05-20");
        config.setEndDate("2021-05-27");
        config.setPreviousBuyDuration(1);
        config.setPreviousSellDuration(1);
        config.setStockTickers(new ArrayList<>(Arrays.asList("TEST")));

        strategy = new StockBuySellStrategy(config);
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 20),3.00f, 2.00f, 3.50f, 1.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 21),2.50f, 1.50f, 3.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 22),1.90f, 2.14f, 2.40f, 1.75f, 3.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 23),2.00f, 3.00f, 4.00f, 0.5f, 2.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 24),3.00f, 4.00f, 4.00f, 0.5f, 3.00f, 500));
        stockDaysList.add(new StockDay("TEST", LocalDate.of(2021, 5, 25),3.40f, 3.60f, 3.60f, 0.5f, 2.00f, 500));
        Mockito.when(repository.findByTickerAndDateBetween("TEST", LocalDate.of(2021, 5, 20), LocalDate.of(2021, 05, 27))).thenReturn(stockDaysList);

        performanceList = service.runSimulation(config);
        performance = performanceList.get(0);

        assertEquals(145.99998f, performance.getRunningGainOrLoss());
        assertEquals(0, performance.getCurNumShares());

    }
}