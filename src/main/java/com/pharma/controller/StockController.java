package com.pharma.controller;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.dto.StockSummaryDto;
import com.pharma.entity.StockItemEntity;
import com.pharma.entity.User;
import com.pharma.service.StockService;
import com.pharma.service.impl.StockSerivceImpl;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/pharma/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockSerivceImpl stockSerivceImpl;

    @Autowired
    private final UserAuthService userAuthService;

    @Autowired
    public StockController(StockService stockService, UserAuthService userAuthService) {
        this.stockService = stockService;
        this.userAuthService = userAuthService;
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> saveStockItems(
            @RequestHeader("Authorization") String token,
            @RequestBody StockDto StockDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        StockDto savedStock = stockService.saveStock(StockDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Stock created successfully", HttpStatus.OK, savedStock);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStocks(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<StockDto> stocks = stockService.getAllStocks(pharmacyId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stocks retrieved successfully", HttpStatus.OK, stocks);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{invId}")
    public ResponseEntity<?> getStockById(
            @RequestHeader("Authorization") String token,
            @PathVariable("invId") UUID invId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        StockDto stockDto = stockService.getStockById(pharmacyId, invId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock retrieved successfully",
                HttpStatus.OK,
                stockDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{invId}")
    public ResponseEntity<?> deleteStockById(
            @RequestHeader("Authorization") String token,
            @PathVariable("invId") UUID invId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

         try {
            stockService.deleteStock(pharmacyId, invId, currentUserOptional.get());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Stock deleted successfully",
                    HttpStatus.OK,
                    null
            );
        } catch (RuntimeException e) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getByItemId/{itemId}")
    public ResponseEntity<?> getStockByItemId(
            @RequestHeader("Authorization") String token,
            @PathVariable("itemId") UUID itemId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }


        List<StockItemDto> stockItems = stockService.getStockByItemId(pharmacyId, itemId,currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock items retrieved successfully",
                HttpStatus.OK,
                stockItems
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/summary")
    public ResponseEntity<?> getStockSummaryByItem(@RequestHeader("Authorization") String token, @RequestParam Long pharmacyId) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<StockDto> stocks = stockService.getAllStocks(pharmacyId, currentUserOptional.get());

        Map<UUID, BigDecimal> amountPerItem = new HashMap<>();

        for (StockDto stock : stocks) {
            for (StockItemDto item : stock.getStockItemDtos()) {
                UUID itemId = item.getItemId();
                BigDecimal amount = item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO;
                amountPerItem.merge(itemId, amount, BigDecimal::add);
            }
        }


        List<Map<String, Object>> response = amountPerItem.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("itemId", entry.getKey());
            map.put("totalAmount", entry.getValue());
            return map;
        }).toList();

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock summary fetched successfully", HttpStatus.OK, response);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/{supplierId}/items")
    public ResponseEntity<?> getItemsBySupplier(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID supplierId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User user = currentUserOptional.get();

        List<StockItemDto> items =
                stockService.getItemsBySupplierId(pharmacyId, supplierId, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock items retrieved successfully",
                HttpStatus.OK,
                items
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/confirmPayment/{invId}")
    public ResponseEntity<?> confirmPayment(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID invId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        try {
            stockService.confirmPayment(pharmacyId, invId, currentUserOptional.get());

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Payment confirmed successfully", HttpStatus.OK, null);
        } catch (RuntimeException e) {
            return ApiResponseHelper.errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/checkBillNo")
    public ResponseEntity<?> checkBillNoExists(
            @RequestHeader("Authorization") String token,
            @RequestParam UUID supplierId,
            @RequestParam Long pharmacyId,
            @RequestParam int year,
            @RequestParam String purchaseBillNo
    ) {
        Optional<User> userOptional = userAuthService.authenticateUser(token);

        if (userOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User user = userOptional.get();

        boolean exists = stockService.isBillNoExists(
                supplierId,
                pharmacyId,
                year,
                purchaseBillNo,
                user
        );

        return ResponseEntity.ok(Map.of("exists", exists));
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/paymentStatusAndSupplierFilter")
    public ResponseEntity<?> getStocksByPaymentStatusAndSupplier(
            @RequestHeader("Authorization") String token,
            @RequestParam String paymentStatus,
            @RequestParam UUID supplierId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User user = currentUserOptional.get();

        List<StockSummaryDto> stocks =
                stockService.getStocksByPaymentStatusAndSupplierAndPharmacy(
                        paymentStatus, supplierId, pharmacyId, user
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stocks retrieved successfully",
                HttpStatus.OK,
                stocks
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/updateStockItem/{invId}/{itemId}/{batchNo}")
    public ResponseEntity<?> updateStockItem(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID invId,
            @PathVariable UUID itemId,
            @PathVariable String batchNo,
            @RequestParam Long pharmacyId,
            @RequestBody StockItemDto updateDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User currentUser = currentUserOptional.get();

        try {
            StockItemDto updated = stockService.updateStockItem(
                    currentUser,
                    currentUser.getId(),
                    pharmacyId,
                    invId,
                    itemId,
                    batchNo,
                    updateDto
            );

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Stock item updated successfully", HttpStatus.OK, updated);

        } catch (RuntimeException e) {
            return ApiResponseHelper.errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
