package com.bensstocksimulator.stockcsvtodb.controller;

import com.bensstocksimulator.stockcsvtodb.helper.CSVHelper;
import com.bensstocksimulator.stockcsvtodb.message.ResponseMessage;
import com.bensstocksimulator.stockcsvtodb.model.StockDay;
import com.bensstocksimulator.stockcsvtodb.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller class for REST operations on uploading/getting CSV file
 * of stock data
 */
@CrossOrigin("http://localhost:8081")
@Controller
@RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    CSVService fileService;

    /**
     *
     * @param file - A valid CSV file
     * @return ResponseEntity - OK if upload valid, EXPECTATION_FAILED if didn't upload
     */
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file")MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                fileService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    /**
     *
     * @return ResponseEntity - Returns all stock day data
     */
    @GetMapping("/stockdays")
    public ResponseEntity<List<StockDay>> getAllStockDays() {
        try {
            List<StockDay> stockDays = fileService.getAllStockDays();

            if (stockDays.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(stockDays, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
