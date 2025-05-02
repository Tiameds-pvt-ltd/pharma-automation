package com.pharma.mapper;

import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.PatientDetailsEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientDetailsMapper {

    public PatientDetailsDto mapToDto(PatientDetailsEntity patientDetailsEntity){
        return new PatientDetailsDto(
                patientDetailsEntity.getPatientId(),
                patientDetailsEntity.getPatientName(),
                patientDetailsEntity.getPatientNumber(),
                patientDetailsEntity.getPatientMobile(),
                patientDetailsEntity.getPatientEmail(),
                patientDetailsEntity.getPatientAddress(),
                patientDetailsEntity.getPharmacyId(),
                patientDetailsEntity.getCreatedBy(),
                patientDetailsEntity.getCreatedDate(),
                patientDetailsEntity.getModifiedBy(),
                patientDetailsEntity.getModifiedDate()
        );
    }

    public static PatientDetailsEntity mapToEntity(PatientDetailsDto patientDetailsDto){
        return new PatientDetailsEntity(
                patientDetailsDto.getPatientId(),
                patientDetailsDto.getPatientName(),
                patientDetailsDto.getPatientNumber(),
                patientDetailsDto.getPatientMobile(),
                patientDetailsDto.getPatientEmail(),
                patientDetailsDto.getPatientAddress(),
                patientDetailsDto.getPharmacyId(),
                patientDetailsDto.getCreatedBy(),
                patientDetailsDto.getCreatedDate(),
                patientDetailsDto.getModifiedBy(),
                patientDetailsDto.getModifiedDate()
        );
    }
}
