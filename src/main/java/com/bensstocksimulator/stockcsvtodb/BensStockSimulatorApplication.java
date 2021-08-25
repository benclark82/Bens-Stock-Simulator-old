package com.bensstocksimulator.stockcsvtodb;

import com.bensstocksimulator.stockcsvtodb.service.CSVService;
import com.bensstocksimulator.stockcsvtodb.service.StockSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BensStockSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BensStockSimulatorApplication.class, args);

    }

}
