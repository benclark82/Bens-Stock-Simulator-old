package com.bensstocksimulator.stockcsvtodb.helper;

import com.bensstocksimulator.stockcsvtodb.model.StockDay;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Helper class for checking to see if CSV file is valid and converting
 * CSV file to daily stock data.  Works with CSV files exported from yahoo
 * finance.
 */
public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERS = { "Ticker", "Date" , "Open", "High", "Low",
            "Close", "Adj Close", "Volume" };


    /**
     * Verifies file is in CSV format
     * @param file
     * @return true if file is CSV, false if not
     */
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    //Convert csv data to StockDay objects

    /**
     *
     * @param is
     * @param ticker
     * @return List<StockDay> - List of stock day objects found in csv file
     */
    public static List<StockDay> csvToStockDay(InputStream is, String ticker) {
        try (BufferedReader fileReader =
                    new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

                        List<StockDay> stockDays = new ArrayList<StockDay>();

                        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                        for (CSVRecord csvRecord : csvRecords) {
                            StockDay stockDay = new StockDay(
                                    ticker,
                                    LocalDate.parse(csvRecord.get("Date")),
                                    Float.parseFloat(csvRecord.get("Open")),
                                    Float.parseFloat(csvRecord.get("High")),
                                    Float.parseFloat(csvRecord.get("Low")),
                                    Float.parseFloat(csvRecord.get("Close")),
                                    Float.parseFloat(csvRecord.get("Adj Close")),
                                    Integer.parseInt(csvRecord.get("Volume"))
                            );

                            stockDays.add(stockDay);
                        }
                        return stockDays;


        } catch(IOException e) {
                        throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }
}
