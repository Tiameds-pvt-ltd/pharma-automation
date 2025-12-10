package com.pharma.service;

import com.pharma.dto.BillReturnDto;
import com.pharma.dto.BillReturnListDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface BillReturnService {

    BillReturnDto createBillReturn(BillReturnDto billReturnDto, User user);

    List<BillReturnDto> getAllBillReturn(Long pharmacyId, User user);

    BillReturnDto getBillReturnById(Long pharmacyId, UUID billReturnId, User user);

    void deleteBillReturn(Long pharmacyId, UUID billReturnId, User user);

//    List<BillReturnListDto> getBillReturnListsByCreatedBy(Long createdById);

    List<BillReturnListDto> getBillReturnListsByPharmacy(Long pharmacyId, User user);

}
