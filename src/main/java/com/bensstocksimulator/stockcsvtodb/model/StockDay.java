package com.bensstocksimulator.stockcsvtodb.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Holds daily stock data
 */
@Entity
@Table(name = "daily_stock_history")
public class StockDay {
    @Id
    private String id;
    private String ticker;
    private LocalDate date;
    private float open;
    private float close;
    private float high;
    private float low;
    @Column(name = "adjusted_close")
    private float adjustedClose;
    @Column(name = "open_close_difference")
    private float openCloseDifference;
    //Absolute value of the open close difference
    @Column(name = "open_close_range_amt")
    private float openCloseRangeAmt;
    private long volume;

    public StockDay() {
    }

    public StockDay(String ticker, LocalDate date, float open, float close, float high,
                    float low, float adjustedClose, long volume) {
        this.id = ticker.concat(date.toString());
        this.ticker = ticker;
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.adjustedClose = adjustedClose;
        this.volume = volume;
        this.openCloseDifference = open - close;
        this.openCloseRangeAmt = Math.abs(openCloseDifference);
    }

    public String getTicker() {
        return ticker;
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

    public float getOpenCloseRangeAmt() {
        return openCloseRangeAmt;
    }

    public long getVolume() {
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
