//package com.pharma.mapper;
//
//import com.pharma.dto.PharmacistDto;
//import com.pharma.entity.Pharmacist;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PharmacistMapper {
//
//    public PharmacistDto toDto(Pharmacist pharmacist) {
//        if (pharmacist == null) {
//            return null;
//        }
//
//        PharmacistDto pharmacistDto = new PharmacistDto();
//        pharmacistDto.setPharmacistId(pharmacist.getPharmacistId());
//        pharmacistDto.setPharmacistName(pharmacist.getPharmacistName());
//        pharmacistDto.setContactNo(pharmacist.getContactNo());
//        pharmacistDto.setEmail(pharmacist.getEmail());
//        pharmacistDto.setAddress(pharmacist.getAddress());
//        pharmacistDto.setZipCode(pharmacist.getZipCode());
//        pharmacistDto.setSecurityId(pharmacist.getSecurityId());
//        pharmacistDto.setSecurityProof(pharmacist.getSecurityProof());
//        pharmacistDto.setCreatedBy(pharmacist.getCreatedBy());
//        pharmacistDto.setCreatedDate(pharmacist.getCreatedDate());
//        pharmacistDto.setModifiedBy(pharmacist.getModifiedBy());
//        pharmacistDto.setModifiedDate(pharmacist.getModifiedDate());
//
//        return pharmacistDto;
//    }
//
//    public Pharmacist toEntity(PharmacistDto pharmacistDto) {
//        if (pharmacistDto == null) {
//            return null;
//        }
//
//        Pharmacist pharmacist = new Pharmacist();
//        pharmacist.setPharmacistId(pharmacistDto.getPharmacistId());
//        pharmacist.setPharmacistName(pharmacistDto.getPharmacistName());
//        pharmacist.setContactNo(pharmacistDto.getContactNo());
//        pharmacist.setEmail(pharmacistDto.getEmail());
//        pharmacist.setAddress(pharmacistDto.getAddress());
//        pharmacist.setZipCode(pharmacistDto.getZipCode());
//        pharmacist.setSecurityId(pharmacistDto.getSecurityId());
//        pharmacist.setSecurityProof(pharmacistDto.getSecurityProof());
//        pharmacist.setCreatedBy(pharmacistDto.getCreatedBy());
//        pharmacist.setCreatedDate(pharmacistDto.getCreatedDate());
//        pharmacist.setModifiedBy(pharmacistDto.getModifiedBy());
//        pharmacist.setModifiedDate(pharmacistDto.getModifiedDate());
//
//        return pharmacist;
//    }
//}
