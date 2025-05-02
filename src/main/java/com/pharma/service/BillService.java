package com.pharma.service;

import com.pharma.dto.BillDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface BillService {
    BillDto createBill(BillDto billDto, User user);

    List<BillDto> getAllBill(Long createdById);

    BillDto getBillById(Long createdById, UUID billId);

//    BillDto updateBill(Long billId, BillDto updatedBill);
    void deleteBill(Long createdById, UUID billId);
}
