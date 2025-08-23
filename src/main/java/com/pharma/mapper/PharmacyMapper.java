package com.pharma.mapper;

import com.pharma.dto.PharmacyDto;
import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;
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
        pharmacyDto.setCity(pharmacy.getCity());
        pharmacyDto.setState(pharmacy.getState());
        pharmacyDto.setDescription(pharmacy.getDescription());
        pharmacyDto.setIsActive(pharmacy.getIsActive());
        pharmacyDto.setGstNo(pharmacy.getGstNo());
        pharmacyDto.setLicenseNo(pharmacy.getLicenseNo());
        pharmacyDto.setPharmaLogo(pharmacy.getPharmaLogo());
        pharmacyDto.setPharmaZip(pharmacy.getPharmaZip());
        pharmacyDto.setPharmaCountry(pharmacy.getPharmaCountry());
        pharmacyDto.setPharmaPhone(pharmacy.getPharmaPhone());
        pharmacyDto.setPharmaEmail(pharmacy.getPharmaEmail());
//        pharmacyDto.setCreatedBy(pharmacy.getCreatedBy());
        pharmacyDto.setCreatedDate(pharmacy.getCreatedDate());
        pharmacyDto.setModifiedBy(pharmacy.getModifiedBy());
        pharmacyDto.setModifiedDate(pharmacy.getModifiedDate());
//        pharmacyDto.setLicenseProof(pharmacy.getLicenseProof());
//        pharmacyDto.setGstProof(pharmacy.getGstProof());

        return pharmacyDto;
    }

    public Pharmacy toEntity(PharmacyDto pharmacyDto, User createdBy) {
        if (pharmacyDto == null) {
            return null;
        }

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setPharmacyId(pharmacyDto.getPharmacyId());
        pharmacy.setName(pharmacyDto.getName());
        pharmacy.setAddress(pharmacyDto.getAddress());
        pharmacy.setCity(pharmacyDto.getCity());
        pharmacy.setState(pharmacyDto.getState());
        pharmacy.setDescription(pharmacyDto.getDescription());
        pharmacy.setIsActive(pharmacyDto.getIsActive());
        pharmacy.setGstNo(pharmacyDto.getGstNo());
        pharmacy.setLicenseNo(pharmacyDto.getLicenseNo());
        pharmacy.setPharmaLogo(pharmacyDto.getPharmaLogo());
        pharmacy.setPharmaZip(pharmacyDto.getPharmaZip());
        pharmacy.setPharmaCountry(pharmacyDto.getPharmaCountry());
        pharmacy.setPharmaPhone(pharmacyDto.getPharmaPhone());
        pharmacy.setPharmaEmail(pharmacyDto.getPharmaEmail());
//        pharmacy.setCreatedBy(pharmacyDto.getCreatedBy());
        pharmacy.setCreatedDate(pharmacyDto.getCreatedDate());
        pharmacy.setModifiedBy(pharmacyDto.getModifiedBy());
        pharmacy.setModifiedDate(pharmacyDto.getModifiedDate());

//        pharmacy.setLicenseProof(pharmacyDto.getLicenseProof());
//        pharmacy.setGstProof(pharmacyDto.getGstProof());
        pharmacy.setCreatedBy(createdBy);
        return pharmacy;
    }
}
