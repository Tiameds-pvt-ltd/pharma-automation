package com.pharma.mapper;

import com.pharma.dto.PharmacyDto;
import com.pharma.entity.Pharmacy;
import org.springframework.stereotype.Component;


@Component
public class PharmacyMapper {

    public PharmacyDto toDto(Pharmacy pharmacy) {
        if (pharmacy == null) {
            return null;
        }

        PharmacyDto pharmacyDto = new PharmacyDto();
        pharmacyDto.setPharmacyId(pharmacy.getPharmacyId());
        pharmacyDto.setName(pharmacy.getName());
        pharmacyDto.setAddress(pharmacy.getAddress());
        pharmacyDto.setZipCode(pharmacy.getZipCode());
        pharmacyDto.setGstNo(pharmacy.getGstNo());
        pharmacyDto.setLicenseNo(pharmacy.getLicenseNo());
        pharmacyDto.setLicenseProof(pharmacy.getLicenseProof());
        pharmacyDto.setGstProof(pharmacy.getGstProof());
        pharmacyDto.setIsActive(pharmacy.getIsActive());
        pharmacyDto.setCreatedBy(pharmacy.getCreatedBy());
        pharmacyDto.setCreatedDate(pharmacy.getCreatedDate());
        pharmacyDto.setModifiedBy(pharmacy.getModifiedBy());
        pharmacyDto.setModifiedDate(pharmacy.getModifiedDate());

        return pharmacyDto;
    }

    public Pharmacy toEntity(PharmacyDto pharmacyDto) {
        if (pharmacyDto == null) {
            return null;
        }

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setPharmacyId(pharmacyDto.getPharmacyId());
        pharmacy.setName(pharmacyDto.getName());
        pharmacy.setAddress(pharmacyDto.getAddress());
        pharmacy.setZipCode(pharmacyDto.getZipCode());
        pharmacy.setGstNo(pharmacyDto.getGstNo());
        pharmacy.setLicenseNo(pharmacyDto.getLicenseNo());
        pharmacy.setLicenseProof(pharmacyDto.getLicenseProof());
        pharmacy.setGstProof(pharmacyDto.getGstProof());
        pharmacy.setIsActive(pharmacyDto.getIsActive());
        pharmacy.setCreatedBy(pharmacyDto.getCreatedBy());
        pharmacy.setCreatedDate(pharmacyDto.getCreatedDate());
        pharmacy.setModifiedBy(pharmacyDto.getModifiedBy());
        pharmacy.setModifiedDate(pharmacyDto.getModifiedDate());

        return pharmacy;
    }
}
