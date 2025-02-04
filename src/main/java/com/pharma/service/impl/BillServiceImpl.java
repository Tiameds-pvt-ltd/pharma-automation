package com.pharma.service.impl;

import com.pharma.dto.BillDto;
import com.pharma.dto.BillItemDto;
import com.pharma.entity.BillEntity;
import com.pharma.entity.BillItemEntity;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.BillMapper;
import com.pharma.repository.BillRepository;
import com.pharma.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Override
    public BillDto createBill(BillDto billDto) {
        BillEntity billEntity = billMapper.toEntity(billDto);
        billEntity.getBillItemEntities().forEach(item -> item.setBillEntity(billEntity));
        BillEntity savedBill = billRepository.save(billEntity);
        return billMapper.toDto(savedBill);    }

    @Override
    public BillDto getBillById(Long billId) {
        BillEntity billEntity = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        return billMapper.toDto(billEntity);
    }


    @Override
    public List<BillDto> getAllBill() {
        return billRepository.findAll().stream()
                .map(billMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BillDto updateBill(Long billId, BillDto updatedBill) {
        BillEntity billEntity = billRepository.findById(billId).
                orElseThrow(() -> new ResourceNotFoundException("Stock does not exists with given ID :" + billId));

        billEntity.setPatientId(updatedBill.getPatientId());
        billEntity.setDoctorId(updatedBill.getDoctorId());
        billEntity.setPatientType(updatedBill.getPatientType());
        billEntity.setSubTotal(updatedBill.getSubTotal());
        billEntity.setTotalGst(updatedBill.getTotalGst());
        billEntity.setTotalDiscount(updatedBill.getTotalDiscount());
        billEntity.setGrandTotal(updatedBill.getGrandTotal());
        billEntity.setPaymentType(updatedBill.getPaymentType());
        billEntity.setBillNo(updatedBill.getBillNo());
        billEntity.setEnteredBy(updatedBill.getEnteredBy());

        List<BillItemEntity> existingItems = billEntity.getBillItemEntities();
        List<BillItemDto> updatedItems = updatedBill.getBillItemDtos();

        existingItems.clear();
        for (BillItemDto itemDTO : updatedItems) {
            BillItemEntity itemEntity = billMapper.toEntity(itemDTO);
            itemEntity.setBillEntity(billEntity);
            existingItems.add(itemEntity);
        }

        BillEntity updatedEntity = billRepository.save(billEntity);
        return billMapper.toDto(updatedEntity);
    }

    @Override
    public void deleteBill(Long billId) {
        BillEntity billEntity = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        billRepository.deleteById(billId);


    }
}
