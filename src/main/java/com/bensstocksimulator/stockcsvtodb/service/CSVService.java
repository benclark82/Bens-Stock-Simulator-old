package com.bensstocksimulator.stockcsvtodb.service;

import com.bensstocksimulator.stockcsvtodb.helper.CSVHelper;
import com.bensstocksimulator.stockcsvtodb.model.StockDay;
import com.bensstocksimulator.stockcsvtodb.repository.StockDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {
    @Autowired
    StockDayRepository repository;

    public void save(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        String ticker = fileName.substring(0, fileName.indexOf('.'));
        try {
            List<StockDay> stockDays = CSVHelper.csvToStockDay(file.getInputStream(), ticker);
            repository.saveAll(stockDays);
        } catch(IOException e) {
            throw new RuntimeException("failed to store csv data: " + e.getMessage());
        }
    }

    public List<StockDay> getAllStockDays() {
        return repository.findAll();
    }
}
