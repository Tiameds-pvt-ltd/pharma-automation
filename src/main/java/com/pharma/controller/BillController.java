package com.pharma.controller;

import com.pharma.dto.BillDto;
import com.pharma.dto.PurchaseOrderDto;
import com.pharma.entity.User;
import com.pharma.service.BillService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/save")
    public ResponseEntity<?> saveBill(
            @RequestHeader("Authorization") String token,
            @RequestBody BillDto billDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        BillDto savedBill = billService.createBill(billDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Bill created successfully", HttpStatus.OK, savedBill);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBills(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        User currentUser = currentUserOptional.get();
        List<BillDto> billDtos = billService.getAllBill(currentUser.getId());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill retrieved successfully", HttpStatus.OK, billDtos);
    }

    @GetMapping("/getById/{billId}")
    public ResponseEntity<?> getBillById(
            @RequestHeader("Authorization") String token,
            @PathVariable("billId") UUID billId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        Long createdById = currentUserOptional.get().getId();
        BillDto billDto = billService.getBillById(createdById, billId);
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase order retrieved successfully",
                HttpStatus.OK,
                billDto
        );
    }


    @DeleteMapping("/delete/{billId}")
    public ResponseEntity<?> deleteBill(
            @RequestHeader("Authorization") String token,
            @PathVariable("billId") UUID billId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        Long createdById = currentUserOptional.get().getId();
        try {
            billService.deleteBill(createdById, billId);
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Bill deleted successfully",
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
}
