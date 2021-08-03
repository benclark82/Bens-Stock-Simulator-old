package com.bensstocksimulator.stockcsvtodb.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockSimulationTest {

    @BeforeEach
    void setUp() {
        StockPerformance stockPerformance = new StockPerformance();
        StockSimulation stockSimulation = new StockSimulation( );
    }

    @Test
    void runSimulation() {
    }
}