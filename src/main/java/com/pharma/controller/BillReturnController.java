package com.pharma.controller;

import com.pharma.dto.BillDto;
import com.pharma.dto.BillReturnDto;
import com.pharma.entity.User;
import com.pharma.service.BillReturnService;
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


@AllArgsConstructor
@RestController
@RequestMapping("/pharma/billReturn")
public class BillReturnController {

    @Autowired
    private BillReturnService billReturnService;

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/save")
    public ResponseEntity<?> saveBill(
            @RequestHeader("Authorization") String token,
            @RequestBody BillReturnDto billReturnDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        BillReturnDto savedBillReturn = billReturnService.createBillReturn(billReturnDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Bill Return created successfully", HttpStatus.OK, savedBillReturn);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBillReturns(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        User currentUser = currentUserOptional.get();
        List<BillReturnDto> billReturnDtos = billReturnService.getAllBillReturn(currentUser.getId());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill Return retrieved successfully", HttpStatus.OK, billReturnDtos);
    }


    @GetMapping("/getById/{billReturnId}")
    public ResponseEntity<?> getBillById(
            @RequestHeader("Authorization") String token,
            @PathVariable("billReturnId") UUID billReturnId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        Long createdById = currentUserOptional.get().getId();
        BillReturnDto billReturnDto = billReturnService.getBillReturnById(createdById, billReturnId);
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill Return retrieved successfully",
                HttpStatus.OK,
                billReturnDto
        );
    }

    @DeleteMapping("/delete/{billReturnId}")
    public ResponseEntity<?> deleteBillReturn(
            @RequestHeader("Authorization") String token,
            @PathVariable("billReturnId") UUID billReturnId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        Long createdById = currentUserOptional.get().getId();
        try {
            billReturnService.deleteBillReturn(createdById, billReturnId);
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Bill Return deleted successfully",
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
