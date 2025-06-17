package com.pharma.mapper;

import com.pharma.dto.BillDto;
import com.pharma.dto.BillItemDto;
import com.pharma.entity.BillEntity;
import com.pharma.entity.BillItemEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BillMapper {

    public BillDto toDto(BillEntity billEntity) {
     BillDto billDto = new BillDto();
        billDto.setBillId(billEntity.getBillId());
        billDto.setBillId1(billEntity.getBillId1());
        billDto.setPharmacyId(billEntity.getPharmacyId());
        billDto.setBillDateTime(billEntity.getBillDateTime());
        billDto.setPatientId(billEntity.getPatientId());
//        billDto.setDoctorId(billEntity.getDoctorId());
        billDto.setDoctorName(billEntity.getDoctorName());
        billDto.setPatientType(billEntity.getPatientType());
        billDto.setSubTotal(billEntity.getSubTotal());
        billDto.setTotalGst(billEntity.getTotalGst());
        billDto.setTotalDiscount(billEntity.getTotalDiscount());
        billDto.setGrandTotal(billEntity.getGrandTotal());
        billDto.setPaymentStatus(billEntity.getPaymentStatus());
        billDto.setPaymentType(billEntity.getPaymentType());
        billDto.setReceivedAmount(billEntity.getReceivedAmount());
        billDto.setBalanceAmount(billEntity.getBalanceAmount());
        billDto.setCreatedBy(billEntity.getCreatedBy());
        billDto.setCreatedDate(billEntity.getCreatedDate());
        billDto.setModifiedBy(billEntity.getModifiedBy());
        billDto.setModifiedDate(billEntity.getModifiedDate());
        billDto.setBillItemDtos(billEntity.getBillItemEntities().stream()
                .map(this::toDto).collect(Collectors.toList()));
        return billDto;
    }

    public BillItemDto toDto(BillItemEntity billItemEntity) {

        BillItemDto billItemDto = new BillItemDto();
        billItemDto.setBillItemId(billItemEntity.getBillItemId());
        billItemDto.setItemId(billItemEntity.getItemId());
        billItemDto.setBatchNo(billItemEntity.getBatchNo());
        billItemDto.setExpiryDate(billItemEntity.getExpiryDate());
        billItemDto.setPackageQuantity(billItemEntity.getPackageQuantity());
        billItemDto.setDiscountPercentage(billItemEntity.getDiscountPercentage());
        billItemDto.setDiscountAmount(billItemEntity.getDiscountAmount());
        billItemDto.setMrpPerUnit(billItemEntity.getMrpPerUnit());
        billItemDto.setGstPercentage(billItemEntity.getGstPercentage());
        billItemDto.setGstAmount(billItemEntity.getGstAmount());
        billItemDto.setGrossTotal(billItemEntity.getGrossTotal());
        billItemDto.setNetTotal(billItemEntity.getNetTotal());
        billItemDto.setCreatedBy(billItemEntity.getCreatedBy());
        billItemDto.setCreatedDate(billItemEntity.getCreatedDate());
        billItemDto.setModifiedBy(billItemEntity.getModifiedBy());
        billItemDto.setModifiedDate(billItemEntity.getModifiedDate());

        return billItemDto;
    }

    public BillEntity toEntity(BillDto billDto) {
        BillEntity billEntity = new BillEntity();
        billEntity.setBillId(billDto.getBillId());
        billEntity.setBillId1(billDto.getBillId1());
        billEntity.setPharmacyId(billDto.getPharmacyId());
        billEntity.setBillDateTime(billDto.getBillDateTime());
        billEntity.setPatientId(billDto.getPatientId());
//        billEntity.setDoctorId(billDto.getDoctorId());
        billEntity.setDoctorName(billDto.getDoctorName());
        billEntity.setPatientType(billDto.getPatientType());
        billEntity.setSubTotal(billDto.getSubTotal());
        billEntity.setTotalGst(billDto.getTotalGst());
        billEntity.setTotalDiscount(billDto.getTotalDiscount());
        billEntity.setGrandTotal(billDto.getGrandTotal());
        billEntity.setPaymentStatus(billDto.getPaymentStatus());
        billEntity.setPaymentType(billDto.getPaymentType());
        billEntity.setReceivedAmount(billDto.getReceivedAmount());
        billEntity.setBalanceAmount(billDto.getBalanceAmount());
        billEntity.setCreatedBy(billDto.getCreatedBy());
        billEntity.setCreatedDate(billDto.getCreatedDate());
        billEntity.setModifiedBy(billDto.getModifiedBy());
        billEntity.setModifiedDate(billDto.getModifiedDate());

        billEntity.setBillItemEntities(billDto.getBillItemDtos().stream()
                .map(this::toEntity).collect(Collectors.toList()));

        return billEntity;
    }


    public BillItemEntity toEntity(BillItemDto billItemDto) {
        BillItemEntity billItemEntity = new BillItemEntity();
        billItemEntity.setBillItemId(billItemDto.getBillItemId());
        billItemEntity.setItemId(billItemDto.getItemId());
        billItemEntity.setBatchNo(billItemDto.getBatchNo());
        billItemEntity.setExpiryDate(billItemDto.getExpiryDate());
        billItemEntity.setPackageQuantity(billItemDto.getPackageQuantity());
        billItemEntity.setDiscountPercentage(billItemDto.getDiscountPercentage());
        billItemEntity.setDiscountAmount(billItemDto.getDiscountAmount());
        billItemEntity.setMrpPerUnit(billItemDto.getMrpPerUnit());
        billItemEntity.setGstPercentage(billItemDto.getGstPercentage());
        billItemEntity.setGstAmount(billItemDto.getGstAmount());
        billItemEntity.setGrossTotal(billItemDto.getGrossTotal());
        billItemEntity.setNetTotal(billItemDto.getNetTotal());
        billItemEntity.setCreatedBy(billItemDto.getCreatedBy());
        billItemEntity.setCreatedDate(billItemDto.getCreatedDate());
        billItemEntity.setModifiedBy(billItemDto.getModifiedBy());
        billItemEntity.setModifiedDate(billItemDto.getModifiedDate());

        return billItemEntity;
    }
}
