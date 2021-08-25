package com.bensstocksimulator.stockcsvtodb.model;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class StockBuySellStrategyTest {

    @InjectMocks
    private  StockSimulationConfiguration stockSimConfig = new StockSimulationConfiguration();
    @InjectMocks
    private StockBuySellStrategy strategy = new StockBuySellStrategy(stockSimConfig);
    @InjectMocks
    private StockPerformance performance = new StockPerformance();

    @Mock
    private StockDay stockDay;
    @Mock
    private List<StockDay> stockDays = new ArrayList<>();


    @BeforeClass
    public void setUp() {
    }

    @Test
    void shouldBuyStock_shouldBuyStock() {
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 1.00f, 2.00f, 3.00f, 0.50f, 2.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.00f, 3.00f, 4.00f, 1.50f, 3.00f, 4000));

        strategy.setBuyLimitPrice(3.99f);
        assertEquals(strategy.shouldBuyStock(stockDays, performance), 3.99f);
    }

    @Test
    void shouldBuyStock_shouldNotBuyStock() {
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 1.00f, 2.00f, 3.00f, 0.50f, 2.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.00f, 3.00f, 4.00f, 1.50f, 3.00f, 4000));

        strategy.setBuyLimitPrice(4.01f);
        assertEquals(strategy.shouldBuyStock(stockDays, performance), 0.0f);
    }


    @Test
    void shouldSellStock_shouldLimitSellStock() {
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 1.00f, 2.00f, 3.00f, 0.50f, 2.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.00f, 3.00f, 4.00f, 1.50f, 3.00f, 4000));

        strategy.setSellLimitPrice(3.99f);
        assertEquals(strategy.shouldSellStock(stockDays, performance), 3.99f);
    }

    @Test
    void shouldSellStock_shouldNotLimitSellStock() {
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 1.00f, 2.00f, 3.00f, 0.50f, 2.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.00f, 3.00f, 4.00f, 1.50f, 3.00f, 4000));

        strategy.setSellLimitPrice(4.01f);
        assertEquals(strategy.shouldSellStock(stockDays, performance), 0.00f);
    }

    @Test
    void whenBullishHaramiFound() {
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(2), 3.00f, 2.00f, 3.00f, 2.00f, 2.00f, 4000));
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 2.00f, 1.00f, 2.00f, 1.00f, 1.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.25f, 2.50f, 2.50f, 2.25f, 2.50f, 4000));

        assertEquals(strategy.bullishHaramiFound(stockDays), true);
    }

    @Test
    void whenBullishHaramiNotFound_lessThan25PercentSmallCandle() {
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(2), 3.00f, 2.00f, 3.00f, 2.00f, 2.00f, 4000));
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 2.00f, 1.00f, 2.00f, 1.00f, 1.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.25f, 2.51f, 2.49f, 2.25f, 2.49f, 4000));

        assertEquals(strategy.bullishHaramiFound(stockDays), false);
    }

    @Test
    void whenBullishHaramiNotFound_notDownwardTrend() {
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(2), 3.00f, 2.00f, 3.00f, 2.00f, 2.00f, 4000));
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 4.00f, 2.00f, 4.00f, 2.00f, 2.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.25f, 2.50f, 2.50f, 2.25f, 2.50f, 4000));

        assertEquals(strategy.bullishHaramiFound(stockDays), false);
    }

    @Test
    void whenBearishHaramiFound() {
        //Setup 2 days ago stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(2), 1.00f, 2.00f, 2.00f, 1.00f, 2.00f, 4000));
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 2.00f, 3.00f, 3.00f, 2.00f, 3.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.50f, 2.25f, 2.25f, 2.25f, 2.25f, 4000));

        assertEquals(strategy.bearishHaramiFound(stockDays), true);
    }

    @Test
    void whenBearishHaramiNotFound_greaterThan25PercentSmallCandle() {
        //Setup 2 days ago stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(2), 1.00f, 2.00f, 2.00f, 1.00f, 2.00f, 4000));
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 2.00f, 3.00f, 3.00f, 2.00f, 3.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.50f, 2.24f, 2.25f, 2.25f, 2.25f, 4000));

        assertEquals(strategy.bearishHaramiFound(stockDays), false);
    }

    @Test
    void whenBearishHaramiNotFound_notUpwardTrend() {
        //Setup 2 days ago stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(2), 4.00f, 3.00f, 4.00f, 3.00f, 3.00f, 4000));
        //Setup yesterday stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now().minusDays(1), 2.00f, 3.00f, 3.00f, 2.00f, 3.00f, 4000));
        //Setup today stockDay
        stockDays.add(new StockDay("AAPL", LocalDate.now(), 2.50f, 2.24f, 2.25f, 2.25f, 2.25f, 4000));

        assertEquals(strategy.bearishHaramiFound(stockDays), false);
    }

    @Test
    void getStockSimConfig() {
    }

    @Test
    void getStockSellPrice() {
    }

    @Test
    void getStockBuyPrice() {
    }

    @Test
    void getStopLossPrice() {
    }

    @Test
    void getSellLimitPrice() {
    }

    @Test
    void setStockSimConfig() {
    }

    @Test
    void setStockSellPrice() {
    }

    @Test
    void setStockBuyPrice() {
    }

    @Test
    void setStopLossPrice() {
    }

    @Test
    void setSellLimitPrice() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void canEqual() {
    }

    @Test
    void testHashCode() {
    }
}