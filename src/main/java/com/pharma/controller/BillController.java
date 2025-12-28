package com.pharma.controller;

import com.pharma.dto.*;
import com.pharma.entity.User;
import com.pharma.repository.BatchWiseProfitDto;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.BillService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestBody BillDto billDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        BillDto savedBill = billService.createBill(billDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Bill created successfully", HttpStatus.OK, savedBill);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/addPayment")
    public ResponseEntity<?> addBillPayment(
            @RequestBody BillPaymentDto billPaymentDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        BillDto updatedBill = billService.addBillPayment(billPaymentDto, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill payment added successfully",
                HttpStatus.OK,
                updatedBill
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBills(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<BillDto> billDtos = billService.getAllBill(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill retrieved successfully", HttpStatus.OK, billDtos);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{billId}")
    public ResponseEntity<?> getBillById(
            @PathVariable("billId") UUID billId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        BillDto billDto = billService.getBillById(pharmacyId, billId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase order retrieved successfully",
                HttpStatus.OK,
                billDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{billId}")
    public ResponseEntity<?> deleteBill(
            @PathVariable("billId") UUID billId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        try {
            billService.deleteBill(pharmacyId, billId, currentUser.getUser());
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
            @RequestParam UUID itemId,
            @RequestParam String batchNo,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        PackageQuantityDto response =
                billService.getPackageQuantityDifference(itemId, batchNo, pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Quantity retrieved successfully",
                HttpStatus.OK,
                response
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/billingSummary")
    public ResponseEntity<?> getSummaryByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        BillingSummaryDto summaryDto =
                billService.getSummaryByDate(pharmacyId, date, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Billing summary retrieved successfully",
                HttpStatus.OK,
                summaryDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/paymentSummary")
    public ResponseEntity<?> getPaymentSummaryByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        PaymentSummaryDto summaryDto =
                billService.getPaymentSummaryByDate(pharmacyId, date, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Payment summary retrieved successfully",
                HttpStatus.OK,
                summaryDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/billGstSummary")
    public ResponseEntity<?> getBillingGstSummary(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "month", required = false) String month,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (date == null && (month == null || month.isBlank())) {
            return ApiResponseHelper.errorResponse("Either date or month is required", HttpStatus.BAD_REQUEST);
        }


        List<BillingGstSummaryDto> summary =
                billService.getBillingGstSummary(pharmacyId, date, month, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Billing GST summary retrieved successfully",
                HttpStatus.OK,
                summary
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getBillsByPatientId/{patientId}")
    public ResponseEntity<?> getBillsByPatientId(
            @PathVariable UUID patientId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<BillDto> bills = billService.getBillsByPatientId(
                pharmacyId,
                patientId,
                currentUser.getUser()
        );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bills retrieved successfully",
                HttpStatus.OK,
                bills
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/itemWiseGrossProfit")
    public ResponseEntity<?> getBatchWiseGrossProfit(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<BatchWiseProfitDto> report =
                billService.getBatchWiseProfitBetweenDates(
                        pharmacyId,
                        fromDate,
                        toDate,
                        currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Batch-wise gross profit report retrieved successfully",
                HttpStatus.OK,
                report
        );
    }

}
