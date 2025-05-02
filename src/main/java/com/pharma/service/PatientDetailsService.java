package com.pharma.service;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface PatientDetailsService {

    PatientDetailsDto createPatient(PatientDetailsDto patientDetailsDto, User user);

    List<PatientDetailsDto> getAllPatient(Long createdById);

    PatientDetailsDto getPatientById(Long createdById, UUID patientId);

    PatientDetailsDto updatePatient(Long modifiedById, UUID patientId, PatientDetailsDto patientDetailsDto);

    void deletePatientById(Long createdById, UUID patientId);


}
