package com.pharma.mapper;

import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.PatientDetailsEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientDetailsMapper {

    public PatientDetailsDto mapToDto(PatientDetailsEntity patientDetailsEntity){
        return new PatientDetailsDto(
                patientDetailsEntity.getPatientId(),
                patientDetailsEntity.getPatientId1(),
                patientDetailsEntity.getPatientName(),
                patientDetailsEntity.getEmail(),
                patientDetailsEntity.getPhone(),
                patientDetailsEntity.getAddress(),
                patientDetailsEntity.getCity(),
                patientDetailsEntity.getState(),
                patientDetailsEntity.getZip(),
                patientDetailsEntity.getBloodGroup(),
                patientDetailsEntity.getDateOfBirth(),
                patientDetailsEntity.getGender(),
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
                patientDetailsDto.getPatientId1(),
                patientDetailsDto.getPatientName(),
                patientDetailsDto.getEmail(),
                patientDetailsDto.getPhone(),
                patientDetailsDto.getAddress(),
                patientDetailsDto.getCity(),
                patientDetailsDto.getState(),
                patientDetailsDto.getZip(),
                patientDetailsDto.getBloodGroup(),
                patientDetailsDto.getDateOfBirth(),
                patientDetailsDto.getGender(),
                patientDetailsDto.getPharmacyId(),
                patientDetailsDto.getCreatedBy(),
                patientDetailsDto.getCreatedDate(),
                patientDetailsDto.getModifiedBy(),
                patientDetailsDto.getModifiedDate()
        );
    }
}
