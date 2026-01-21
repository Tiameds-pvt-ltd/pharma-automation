package com.pharma.service;

import com.pharma.dto.*;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface BillItemService {

    List<ItemProfitRowDto> getItemProfitByMonth(UUID itemId, Long pharmacyId, String monthYear, User user);

    List<GstSlabNetPayableDto> getNetGstSlabWise(Long pharmacyId, String monthYear, User user);

    List<ItemProfitByDoctorDto> getDoctorWiseItemProfit(UUID doctorId, Long pharmacyId, String monthYear, User user);

    List<DailySalesCostProfitDto> getDailySalesCostProfit(Long pharmacyId, String monthYear, User user);

    List<ItemProfitSummaryDto> getItemWiseProfitSummary(Long pharmacyId, String monthYear, String dateRange, User user);

    List<ItemPatientBillDto> getItemPatientBillDetails(UUID itemId, Long pharmacyId, String monthYear, String dateRange, User user);

    List<ItemDayWiseSaleDto> getItemDayWiseSales(Long pharmacyId, String monthYear, String dateRange, User user);
}
