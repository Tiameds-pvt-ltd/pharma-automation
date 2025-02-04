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
        billDto.setBillDate(billEntity.getBillDate());
        billDto.setBillTime(billEntity.getBillTime());
        billDto.setPatientId(billEntity.getPatientId());
        billDto.setDoctorId(billEntity.getDoctorId());
        billDto.setPatientType(billEntity.getPatientType());
        billDto.setSubTotal(billEntity.getSubTotal());
        billDto.setTotalGst(billEntity.getTotalGst());
        billDto.setTotalDiscount(billEntity.getTotalDiscount());
        billDto.setGrandTotal(billEntity.getGrandTotal());
        billDto.setPaymentType(billEntity.getPaymentType());
        billDto.setBillNo(billEntity.getBillNo());
        billDto.setEnteredBy(billEntity.getEnteredBy());
        billDto.setBillItemDtos(billEntity.getBillItemEntities().stream()
                .map(this::toDto).collect(Collectors.toList()));
        return billDto;
    }

    public BillItemDto toDto(BillItemEntity billItemEntity) {

        BillItemDto billItemDto = new BillItemDto();
        billItemDto.setBillItemId(billItemEntity.getBillItemId());
//        billItemDto.setBillId(billItemEntity.getBillId());
        billItemDto.setItemId(billItemEntity.getItemId());
        billItemDto.setBatchNo(billItemEntity.getBatchNo());
        billItemDto.setExpiryDate(billItemEntity.getExpiryDate());
        billItemDto.setQuantity(billItemEntity.getQuantity());
        billItemDto.setDiscount(billItemEntity.getDiscount());
        billItemDto.setMrp(billItemEntity.getMrp());
        billItemDto.setGstPercentage(billItemEntity.getGstPercentage());
        billItemDto.setGrossTotal(billItemEntity.getGrossTotal());
        billItemDto.setNetTotal(billItemEntity.getNetTotal());

        return billItemDto;
    }

    public BillEntity toEntity(BillDto billDto) {
        BillEntity billEntity = new BillEntity();
        billEntity.setBillId(billDto.getBillId());
        billEntity.setBillDate(billDto.getBillDate());
        billEntity.setBillTime(billDto.getBillTime());
        billEntity.setPatientId(billDto.getPatientId());
        billEntity.setDoctorId(billDto.getDoctorId());
        billEntity.setPatientType(billDto.getPatientType());
        billEntity.setSubTotal(billDto.getSubTotal());
        billEntity.setTotalGst(billDto.getTotalGst());
        billEntity.setTotalDiscount(billDto.getTotalDiscount());
        billEntity.setGrandTotal(billDto.getGrandTotal());
        billEntity.setPaymentType(billDto.getPaymentType());
        billEntity.setBillNo(billDto.getBillNo());
        billEntity.setEnteredBy(billDto.getEnteredBy());
        billEntity.setBillItemEntities(billDto.getBillItemDtos().stream()
                .map(this::toEntity).collect(Collectors.toList()));

        return billEntity;
    }


    public BillItemEntity toEntity(BillItemDto billItemDto) {
        BillItemEntity billItemEntity = new BillItemEntity();
        billItemEntity.setBillItemId(billItemDto.getBillItemId());
//        billItemEntity.setBillId(billItemDto.getBillId());
        billItemEntity.setItemId(billItemDto.getItemId());
        billItemEntity.setBatchNo(billItemDto.getBatchNo());
        billItemEntity.setExpiryDate(billItemDto.getExpiryDate());
        billItemEntity.setQuantity(billItemDto.getQuantity());
        billItemEntity.setDiscount(billItemDto.getDiscount());
        billItemEntity.setMrp(billItemDto.getMrp());
        billItemEntity.setGstPercentage(billItemDto.getGstPercentage());
        billItemEntity.setGrossTotal(billItemDto.getGrossTotal());
        billItemEntity.setNetTotal(billItemDto.getNetTotal());

        return billItemEntity;
    }
}
