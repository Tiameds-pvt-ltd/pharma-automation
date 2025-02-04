package com.pharma.service;

import com.pharma.dto.PatientDetailsDto;

import java.util.List;
import java.util.Optional;

public interface PatientDetailsService {

    PatientDetailsDto createPatient(PatientDetailsDto patientDetailsDto);

    PatientDetailsDto getPatientById(Long patientId);

    List<PatientDetailsDto> getAllPatient();

    PatientDetailsDto updatePatient(Long patientId, PatientDetailsDto updatePatient);

    void deletePatient(Long patientId);


}
