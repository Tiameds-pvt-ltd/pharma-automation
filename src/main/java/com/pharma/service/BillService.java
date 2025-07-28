package com.pharma.service;

import com.pharma.dto.BillDto;
import com.pharma.dto.InventoryDto;
import com.pharma.dto.PackageQuantityDto;
import com.pharma.entity.User;

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
}
