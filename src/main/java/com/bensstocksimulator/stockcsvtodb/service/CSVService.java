package com.bensstocksimulator.stockcsvtodb.service;

import com.bensstocksimulator.stockcsvtodb.helper.CSVHelper;
import com.bensstocksimulator.stockcsvtodb.model.StockDay;
import com.bensstocksimulator.stockcsvtodb.model.StockSimulationConfiguration;
import com.bensstocksimulator.stockcsvtodb.repository.StockDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

@Service
public class CSVService {
    private final StockDayRepository repository;
    private final ResourcePatternResolver resourcePatternResolver;
    private final StockSimulationConfiguration simConfig;

    public CSVService(StockDayRepository repository, ResourcePatternResolver resourcePatternResolver,
                      StockSimulationConfiguration simConfig) {
        this.repository = repository;
        this.resourcePatternResolver = resourcePatternResolver;
        this.simConfig = simConfig;

        if(simConfig.isLoadCsvFiles())
            this.loadAllCsvFiles();
    }

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

    public void loadAllCsvFiles() {
        MultipartFile multipartFile;
        FileInputStream input;

        System.out.println("Loading all stock CSV files....");
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:stocks/*.csv");

            for (Resource resource: resources ) {
                System.out.println("Loading " + resource.getFile().getName());
                input = new FileInputStream(resource.getFile());

                multipartFile = new MockMultipartFile("file",
                        resource.getFile().getName(), "text/csv", input);

                save(multipartFile);
                System.out.println("Saving " + resource.getFile().getName());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        assert(true);

    }

    public List<StockDay> getAllStockDays() {
        return repository.findAll();
    }
}
