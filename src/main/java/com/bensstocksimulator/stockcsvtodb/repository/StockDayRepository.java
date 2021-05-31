package com.bensstocksimulator.stockcsvtodb.repository;

import com.bensstocksimulator.stockcsvtodb.model.StockDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockDayRepository extends JpaRepository<StockDay, Long> {
}
