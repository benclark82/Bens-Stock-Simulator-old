package com.bensstocksimulator.stockcsvtodb.controller;

import com.bensstocksimulator.stockcsvtodb.model.StockPerformance;
import com.bensstocksimulator.stockcsvtodb.model.StockSimulationConfiguration;
import com.bensstocksimulator.stockcsvtodb.service.StockSimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/simulator")
public class SimulatorController {

    StockSimulationService simulationService;

    public SimulatorController(StockSimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping(value = "/run",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockPerformance>> runSimulation(@RequestBody StockSimulationConfiguration config) {
        ArrayList<StockPerformance> stockPerformances = simulationService.runSimulation(config);

        return new ResponseEntity<List<StockPerformance>>(stockPerformances, HttpStatus.OK);
    }

}
