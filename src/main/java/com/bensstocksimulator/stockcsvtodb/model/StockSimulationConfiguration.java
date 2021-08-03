package com.bensstocksimulator.stockcsvtodb.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class StockSimulationConfiguration {

    public StockSimulationConfiguration() {
        this.buyIfPreviousDayUp = false;
        this.sellIfPreviousDayUp = false;
        this.buyIfPreviousDayDown = false;
        this.sellIfPreviousDayDown = false;
        this.buyIfBullishHarami = false;
        this.isSellIfBullishHarami = false;
        this.buyIfBearishHarami = false;
        this.sellIfBearishHarami = false;
    }

    private boolean buyIfPreviousDayUp;
    private boolean sellIfPreviousDayUp;
    private boolean buyIfPreviousDayDown;
    private boolean sellIfPreviousDayDown;
    private boolean buyIfBullishHarami;
    private boolean isSellIfBullishHarami;
    private boolean buyIfBearishHarami;
    private boolean sellIfBearishHarami;

}
