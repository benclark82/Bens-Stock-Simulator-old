package com.bensstocksimulator.stockcsvtodb.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Holds daily stock data
 */
@Entity
@Table(name = "daily_stock_history")
public class StockDay {
    @Id
    @GeneratedValue
    private Long id;

    private String ticker;
    private LocalDate date;
    private float open;
    private float close;
    private float high;
    private float low;
    private float adjustedClose;
    private float openCloseRangeAmt;
    private int volume;

    public StockDay() {
    }

    public StockDay(String ticker, LocalDate date,
                    float open, float close, float high,
                    float low, float adjustedClose, int volume) {
        this.ticker = ticker;
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.adjustedClose = adjustedClose;
        this.openCloseRangeAmt = Math.abs(close - open);
        this.volume = volume;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(float adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    public float getOpenCloseRangeAmt() {
        return openCloseRangeAmt;
    }

    public void setOpenCloseRangeAmt(float openCloseRangeAmt) {
        this.openCloseRangeAmt = openCloseRangeAmt;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "StockDay{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", date=" + date +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", adjustedClose=" + adjustedClose +
                ", volume=" + volume +
                '}';
    }
}
