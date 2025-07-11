package com.pharma.service;

import com.pharma.dto.BillReturnDto;
import com.pharma.dto.BillReturnListDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface BillReturnService {

    BillReturnDto createBillReturn(BillReturnDto billReturnDto, User user);

    List<BillReturnDto> getAllBillReturn(Long createdById);

    BillReturnDto getBillReturnById(Long createdById, UUID billReturnId);

    void deleteBillReturn(Long createdById, UUID billReturnId);

    List<BillReturnListDto> getBillReturnListsByCreatedBy(Long createdById);
}
