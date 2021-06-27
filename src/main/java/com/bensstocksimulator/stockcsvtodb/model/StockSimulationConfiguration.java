package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class StockSimulationConfiguration {
    private boolean buyIfPreviousDayUp;
    private boolean sellIfPreviousDayUp;
    private boolean buyIfPreviousDayDown;
    private boolean sellIfPreviousDayDown;
    private boolean buyIfBullishHarami;
    private boolean isSellIfBullishHarami;
    private boolean buyIfBearishHarami;
    private boolean sellIfBearishHarami;

}
