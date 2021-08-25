package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="simulation.config")
public class StockSimulationConfiguration {

    private boolean buyIfPreviouslyUp;
    private boolean buyIfPreviouslyDown;
    private boolean buyIfBullishHarami;
    private boolean buyIfBearishHarami;
    private boolean sellIfPreviouslyUp;
    private boolean sellIfPreviouslyDown;
    private boolean sellIfBullishHarami;
    private boolean sellIfBearishHarami;
    private String startDate;
    private String endDate;
    private int previousBuyDuration;
    private int previousSellDuration;
    private boolean loadCsvFiles;
    ArrayList<String> stockTickers;

}
