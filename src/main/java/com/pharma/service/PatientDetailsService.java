package com.pharma.service;

import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface PatientDetailsService {

    PatientDetailsDto createPatient(PatientDetailsDto patientDetailsDto, User user);

    List<PatientDetailsDto> getAllPatient(Long pharmacyId, User user);

    PatientDetailsDto getPatientById(Long pharmacyId, UUID patientId, User user);

    PatientDetailsDto updatePatient(Long pharmacyId, UUID patientId, PatientDetailsDto patientDetailsDto, User user);

    void deletePatientById(Long pharmacyId, UUID patientId, User user);


}
