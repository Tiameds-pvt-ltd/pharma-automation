package com.pharma.service;

import com.pharma.dto.*;
import com.pharma.entity.User;
import com.pharma.repository.BatchWiseProfitDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BillService {
    BillDto createBill(BillDto billDto, User user);

    BillDto addBillPayment(BillPaymentDto billPaymentDto, User user);

    List<BillDto> getAllBill(Long pharmacyId, User user);

    BillDto getBillById(Long pharmacyId, UUID billId, User user);

    void deleteBill(Long pharmacyId, UUID billId, User user);

    interface InventoryDetailsService {

        List<InventoryDto> getAllInventoryDetails(Long createdById);

    }

    PackageQuantityDto getPackageQuantityDifference(UUID itemId, String batchNo, Long pharmacyId, User user);

    BillingSummaryDto getSummaryByDate(Long pharmacyId, LocalDate selectedDate, User user);

    PaymentSummaryDto getPaymentSummaryByDate(Long pharmacyId, LocalDate selectedDate, User user);

    List<BillingGstSummaryDto> getBillingGstSummary(Long pharmacyId, LocalDate inputDate, String inputMonth, User user);

    List<BillDto> getBillsByPatientId(Long pharmacyId, UUID patientId, User user);

    List<BatchWiseProfitDto> getBatchWiseProfitBetweenDates(Long pharmacyId, LocalDate fromDate, LocalDate toDate, User user
    );

}
