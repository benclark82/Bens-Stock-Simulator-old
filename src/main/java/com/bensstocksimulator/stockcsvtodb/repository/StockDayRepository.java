package com.bensstocksimulator.stockcsvtodb.repository;

import com.bensstocksimulator.stockcsvtodb.model.StockDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface StockDayRepository extends JpaRepository<StockDay, Long> {
    public ArrayList<StockDay> findByTickerAndDateBetween(String ticker, LocalDate startDate, LocalDate endDate);
}
