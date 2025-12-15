package com.pharma.controller;

import com.pharma.dto.*;
import com.pharma.entity.User;
import com.pharma.service.BillService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/addPayment")
    public ResponseEntity<?> addBillPayment(
            @RequestHeader("Authorization") String token,
            @RequestBody BillPaymentDto billPaymentDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token",
                    HttpStatus.UNAUTHORIZED,
                    null
            );
        }

        BillDto updatedBill = billService.addBillPayment(billPaymentDto, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill payment added successfully",
                HttpStatus.OK,
                updatedBill
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBills(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<BillDto> billDtos = billService.getAllBill(pharmacyId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill retrieved successfully", HttpStatus.OK, billDtos);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{billId}")
    public ResponseEntity<?> getBillById(
            @RequestHeader("Authorization") String token,
            @PathVariable("billId") UUID billId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        BillDto billDto = billService.getBillById(pharmacyId, billId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase order retrieved successfully",
                HttpStatus.OK,
                billDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{billId}")
    public ResponseEntity<?> deleteBill(
            @RequestHeader("Authorization") String token,
            @PathVariable("billId") UUID billId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        try {
            billService.deleteBill(pharmacyId, billId, currentUserOptional.get());
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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getPackageQuantity")
    public ResponseEntity<?> getPackageQuantityDifference(
            @RequestHeader("Authorization") String token,
            @RequestParam UUID itemId,
            @RequestParam String batchNo,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> userOptional = userAuthService.authenticateUser(token);

        if (userOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User user = userOptional.get();

        PackageQuantityDto response =
                billService.getPackageQuantityDifference(itemId, batchNo, pharmacyId, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Quantity retrieved successfully",
                HttpStatus.OK,
                response
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/billingSummary")
    public ResponseEntity<?> getSummaryByDate(
            @RequestHeader("Authorization") String token,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User user = currentUserOptional.get();

        BillingSummaryDto summaryDto =
                billService.getSummaryByDate(pharmacyId, date, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Billing summary retrieved successfully",
                HttpStatus.OK,
                summaryDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/paymentSummary")
    public ResponseEntity<?> getPaymentSummaryByDate(
            @RequestHeader("Authorization") String token,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User user = currentUserOptional.get();

        PaymentSummaryDto summaryDto =
                billService.getPaymentSummaryByDate(pharmacyId, date, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Payment summary retrieved successfully",
                HttpStatus.OK,
                summaryDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/billGstSummary")
    public ResponseEntity<?> getBillingGstSummary(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "month", required = false) String month,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        if (date == null && (month == null || month.isBlank())) {
            return ApiResponseHelper.errorResponse("Either date or month is required", HttpStatus.BAD_REQUEST);
        }

        User user = currentUserOptional.get();

        List<BillingGstSummaryDto> summary =
                billService.getBillingGstSummary(pharmacyId, date, month, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Billing GST summary retrieved successfully",
                HttpStatus.OK,
                summary
        );
    }

}
