package com.pharma.service;

import com.pharma.dto.*;
import com.pharma.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BillService {
    BillDto createBill(BillDto billDto, User user);

    List<BillDto> getAllBill(Long createdById);

    BillDto getBillById(Long createdById, UUID billId);

    void deleteBill(Long createdById, UUID billId);

    interface InventoryDetailsService {

        List<InventoryDto> getAllInventoryDetails(Long createdById);

    }

    PackageQuantityDto getPackageQuantityDifference(String itemId, String batchNo);

    BillingSummaryDto getSummaryByDate(Long createdBy, LocalDate selectedDate);

    PaymentSummaryDto getPaymentSummaryByDate(Long createdBy, LocalDate selectedDate);

    List<BillingGstSummaryDto> getBillingGstSummary(Long createdBy, LocalDate inputDate, String inputMonth);


}
