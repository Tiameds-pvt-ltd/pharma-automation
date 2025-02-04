package com.pharma.service;

import com.pharma.dto.BillDto;

import java.util.List;

public interface BillService {
    BillDto createBill(BillDto billDto);

    BillDto getBillById(Long billId);

    List<BillDto> getAllBill();

    BillDto updateBill(Long billId, BillDto updatedBill);

    void deleteBill(Long billId);
}
