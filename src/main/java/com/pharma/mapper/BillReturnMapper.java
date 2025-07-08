package com.pharma.mapper;


import com.pharma.dto.BillReturnItemDto;
import com.pharma.dto.BillReturnDto;
import com.pharma.entity.BillReturnItemEntity;
import com.pharma.entity.BillReturnEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BillReturnMapper {

    public BillReturnDto toDto(BillReturnEntity billReturnEntity) {
        BillReturnDto billReturnDto = new BillReturnDto();
        billReturnDto.setBillReturnId(billReturnEntity.getBillReturnId());
        billReturnDto.setBillReturnId1(billReturnEntity.getBillReturnId1());
        billReturnDto.setPharmacyId(billReturnEntity.getPharmacyId());
        billReturnDto.setBillReturnDateTime(billReturnEntity.getBillReturnDateTime());
        billReturnDto.setBillId1(billReturnEntity.getBillId1());
        billReturnDto.setPatientId(billReturnEntity.getPatientId());
        billReturnDto.setDoctorId(billReturnEntity.getDoctorId());
        billReturnDto.setDoctorName(billReturnEntity.getDoctorName());
        billReturnDto.setPatientType(billReturnEntity.getPatientType());
        billReturnDto.setSubTotal(billReturnEntity.getSubTotal());
        billReturnDto.setTotalGst(billReturnEntity.getTotalGst());
        billReturnDto.setGrandTotal(billReturnEntity.getGrandTotal());
        billReturnDto.setCreatedBy(billReturnEntity.getCreatedBy());
        billReturnDto.setCreatedDate(billReturnEntity.getCreatedDate());
        billReturnDto.setModifiedBy(billReturnEntity.getModifiedBy());
        billReturnDto.setModifiedDate(billReturnEntity.getModifiedDate());
        billReturnDto.setBillReturnItemDtos(billReturnEntity.getBillReturnItemEntities().stream()
                .map(this::toDto).collect(Collectors.toList()));
        return billReturnDto;
    }

    public BillReturnItemDto toDto(BillReturnItemEntity billReturnItemEntity) {

        BillReturnItemDto billReturnItemDto = new BillReturnItemDto();
        billReturnItemDto.setBillReturnItemId(billReturnItemEntity.getBillReturnItemId());
        billReturnItemDto.setItemId(billReturnItemEntity.getItemId());
        billReturnItemDto.setBatchNo(billReturnItemEntity.getBatchNo());
        billReturnItemDto.setExpiryDate(billReturnItemEntity.getExpiryDate());
        billReturnItemDto.setPackageQuantity(billReturnItemEntity.getPackageQuantity());
        billReturnItemDto.setReturnQuantity(billReturnItemEntity.getReturnQuantity());
        billReturnItemDto.setMrpSalePricePerUnit(billReturnItemEntity.getMrpSalePricePerUnit());
        billReturnItemDto.setGstPercentage(billReturnItemEntity.getGstPercentage());
        billReturnItemDto.setGstAmount(billReturnItemEntity.getGstAmount());
        billReturnItemDto.setGrossTotal(billReturnItemEntity.getGrossTotal());
        billReturnItemDto.setNetTotal(billReturnItemEntity.getNetTotal());
        billReturnItemDto.setCreatedBy(billReturnItemEntity.getCreatedBy());
        billReturnItemDto.setCreatedDate(billReturnItemEntity.getCreatedDate());
        billReturnItemDto.setModifiedBy(billReturnItemEntity.getModifiedBy());
        billReturnItemDto.setModifiedDate(billReturnItemEntity.getModifiedDate());

        return billReturnItemDto;
    }

    public BillReturnEntity toEntity(BillReturnDto billReturnDto) {
        BillReturnEntity billReturnEntity = new BillReturnEntity();
        billReturnEntity.setBillReturnId(billReturnDto.getBillReturnId());
        billReturnEntity.setBillReturnId1(billReturnDto.getBillReturnId1());
        billReturnEntity.setPharmacyId(billReturnDto.getPharmacyId());
        billReturnEntity.setBillReturnDateTime(billReturnDto.getBillReturnDateTime());
        billReturnEntity.setBillId1(billReturnDto.getBillId1());
        billReturnEntity.setPatientId(billReturnDto.getPatientId());
        billReturnEntity.setDoctorId(billReturnDto.getDoctorId());
        billReturnEntity.setDoctorName(billReturnDto.getDoctorName());
        billReturnEntity.setPatientType(billReturnDto.getPatientType());
        billReturnEntity.setSubTotal(billReturnDto.getSubTotal());
        billReturnEntity.setTotalGst(billReturnDto.getTotalGst());
        billReturnEntity.setGrandTotal(billReturnDto.getGrandTotal());
        billReturnEntity.setCreatedBy(billReturnDto.getCreatedBy());
        billReturnEntity.setCreatedDate(billReturnDto.getCreatedDate());
        billReturnEntity.setModifiedBy(billReturnDto.getModifiedBy());
        billReturnEntity.setModifiedDate(billReturnDto.getModifiedDate());

        billReturnEntity.setBillReturnItemEntities(billReturnDto.getBillReturnItemDtos().stream()
                .map(this::toEntity).collect(Collectors.toList()));

        return billReturnEntity;
    }

    public BillReturnItemEntity toEntity(BillReturnItemDto billReturnItemDto) {
        BillReturnItemEntity billReturnItemEntity = new BillReturnItemEntity();
        billReturnItemEntity.setBillReturnItemId(billReturnItemDto.getBillReturnItemId());
        billReturnItemEntity.setItemId(billReturnItemDto.getItemId());
        billReturnItemEntity.setBatchNo(billReturnItemDto.getBatchNo());
        billReturnItemEntity.setExpiryDate(billReturnItemDto.getExpiryDate());
        billReturnItemEntity.setPackageQuantity(billReturnItemDto.getPackageQuantity());
        billReturnItemEntity.setReturnQuantity(billReturnItemDto.getReturnQuantity());
        billReturnItemEntity.setMrpSalePricePerUnit(billReturnItemDto.getMrpSalePricePerUnit());
        billReturnItemEntity.setGstPercentage(billReturnItemDto.getGstPercentage());
        billReturnItemEntity.setGstAmount(billReturnItemDto.getGstAmount());
        billReturnItemEntity.setGrossTotal(billReturnItemDto.getGrossTotal());
        billReturnItemEntity.setNetTotal(billReturnItemDto.getNetTotal());
        billReturnItemEntity.setCreatedBy(billReturnItemDto.getCreatedBy());
        billReturnItemEntity.setCreatedDate(billReturnItemDto.getCreatedDate());
        billReturnItemEntity.setModifiedBy(billReturnItemDto.getModifiedBy());
        billReturnItemEntity.setModifiedDate(billReturnItemDto.getModifiedDate());

        return billReturnItemEntity;
    }
}
