package com.project.pharma.controller;

import com.project.pharma.dto.StockDto;
import com.project.pharma.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/save")
    public ResponseEntity<StockDto> saveStock(@RequestBody StockDto StockDto) {
        StockDto savedStock = stockService.createStock(StockDto);
        return ResponseEntity.ok(savedStock);
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<StockDto> getStockById(@PathVariable("id") Integer invId) {
        return ResponseEntity.ok(stockService.getStockById(invId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<StockDto>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StockDto> updateStock(@PathVariable("id") Integer invId, @RequestBody StockDto updatedStock) {
        StockDto updatedStockResponse = stockService.updateStock(invId, updatedStock);
        return new ResponseEntity<>(updatedStockResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable("id") Integer invId){
        stockService.deleteStock(invId);
        return ResponseEntity.ok("Stock Deleted Successfully");
    }
}
