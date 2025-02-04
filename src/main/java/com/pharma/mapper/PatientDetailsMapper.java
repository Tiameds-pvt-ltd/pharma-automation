package com.pharma.mapper;

import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.PatientDetailsEntity;

public class PatientDetailsMapper {

    public static PatientDetailsDto mapToDto(PatientDetailsEntity patientDetailsEntity){
        return new PatientDetailsDto(
                patientDetailsEntity.getPatientId(),
                patientDetailsEntity.getPatientName(),
                patientDetailsEntity.getPatientNumber(),
                patientDetailsEntity.getPatientMobile(),
                patientDetailsEntity.getPatientAddress(),
                patientDetailsEntity.getPatientEnteredDate(),
                patientDetailsEntity.getPatientEnteredBy()

        );
    }

    public static PatientDetailsEntity mapToEntity(PatientDetailsDto patientDetailsDto){
        return new PatientDetailsEntity(
                patientDetailsDto.getPatientId(),
                patientDetailsDto.getPatientName(),
                patientDetailsDto.getPatientNumber(),
                patientDetailsDto.getPatientMobile(),
                patientDetailsDto.getPatientAddress(),
                patientDetailsDto.getPatientEnteredDate(),
                patientDetailsDto.getPatientEnteredBy()
        );
    }
}
