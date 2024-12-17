package com.pharma.controller;


import com.pharma.dto.StockDto;
import com.pharma.entity.User;
import com.pharma.service.StockService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/pharma/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    private final UserAuthService userAuthService;

    @Autowired
    public StockController(StockService stockService, UserAuthService userAuthService) {
        this.stockService = stockService;
        this.userAuthService = userAuthService;
    }

//    @PostMapping("/save")
//    public ResponseEntity<StockDto> saveStock(
//            @RequestHeader("Authorization") String token,
//            @RequestBody StockDto StockDto) {
//        StockDto savedStock = stockService.createStock(StockDto);
//        return ResponseEntity.ok(savedStock);
//    }

//    @GetMapping("/getById/{id}")
//    public ResponseEntity<StockDto> getStockById(@PathVariable("id") Integer invId) {
//        return ResponseEntity.ok(stockService.getStockById(invId));
//    }

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

    //create stock items for a user
    @PostMapping("/save")
    public ResponseEntity<?> saveStockItems(
            @RequestHeader("Authorization") String token,
            @RequestBody StockDto StockDto
    ) {
        // Validate token format
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        //service call
        StockDto savedStock = stockService.createStockAndAssociateWithUser(StockDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Stock created successfully", HttpStatus.OK, savedStock);
    }


}
