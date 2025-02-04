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
import java.util.Map;
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

    @PostMapping("/save")
    public ResponseEntity<?> saveStockItems(
            @RequestHeader("Authorization") String token,
            @RequestBody StockDto StockDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        StockDto savedStock = stockService.createStockAndAssociateWithUser(StockDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Stock created successfully", HttpStatus.OK, savedStock);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStocks(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        User currentUser = currentUserOptional.get();
        List<StockDto> userStocks = stockService.getAllStocks(String.valueOf(currentUser.getId()));

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stocks retrieved successfully", HttpStatus.OK, userStocks);
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getStockById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long invId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        String userId = String.valueOf(currentUserOptional.get().getId());
        StockDto stockDto = stockService.getStockById(userId, invId);
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock retrieved successfully",
                HttpStatus.OK,
                stockDto
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StockDto> updateStock(@PathVariable("id") Long invId, @RequestBody StockDto updatedStock) {
        StockDto updatedStockResponse = stockService.updateStock(invId, updatedStock);
        return new ResponseEntity<>(updatedStockResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStock(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long invId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        stockService.deleteStock(invId, currentUserOptional.get().getId().toString());
        return ApiResponseHelper.successResponseWithDataAndMessage("Stock deleted successfully", HttpStatus.OK, null);
    }

    @GetMapping("/checkBillNo")
    public ResponseEntity<Map<String, Boolean>> checkBillNoExists(
            @RequestParam("supplierId") Long supplierId,
            @RequestParam("year") int year,
            @RequestParam("purchaseBillNo") String purchaseBillNo) {
        try {
            boolean exists = stockService.isBillNoExists(supplierId, year, purchaseBillNo);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", true));
        }
    }
}
