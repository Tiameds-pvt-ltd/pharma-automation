package com.pharma.service.impl;

import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.PatientDetailsEntity;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.PatientDetailsMapper;
import com.pharma.repository.PatientDetailsRepository;
import com.pharma.service.PatientDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientDetailsServiceImpl implements PatientDetailsService {

    private PatientDetailsRepository patientDetailsRepository;

    @Override
    public PatientDetailsDto createPatient(PatientDetailsDto patientDetailsDto) {
        PatientDetailsEntity patientDetailsEntity = PatientDetailsMapper.mapToEntity(patientDetailsDto);
        PatientDetailsEntity savePatient = patientDetailsRepository.save(patientDetailsEntity);
         return PatientDetailsMapper.mapToDto(savePatient);
    }

    @Override
    public PatientDetailsDto getPatientById(Long patientId) {
        PatientDetailsEntity patientDetailsEntity = patientDetailsRepository.findById(patientId).
                orElseThrow(() -> new ResourceNotFoundException("Patient does not exists with the given ID : " + patientId));
        return PatientDetailsMapper.mapToDto(patientDetailsEntity);
    }

    @Override
    public List<PatientDetailsDto> getAllPatient() {
        List<PatientDetailsEntity> patientDetailsEntities = patientDetailsRepository.findAll();
        return patientDetailsEntities.stream().map((patientDetailsEntity) -> PatientDetailsMapper.mapToDto(patientDetailsEntity)).collect(Collectors.toList());
    }

    @Override
    public PatientDetailsDto updatePatient(Long patientId, PatientDetailsDto updatePatient) {
        PatientDetailsEntity patientDetailsEntity = patientDetailsRepository.findById(patientId).
                orElseThrow(() -> new ResourceNotFoundException("Patient does not exists with the given ID : " + patientId));

          patientDetailsEntity.setPatientName(updatePatient.getPatientName());
          patientDetailsEntity.setPatientMobile(updatePatient.getPatientMobile());
          patientDetailsEntity.setPatientAddress(updatePatient.getPatientAddress());

          PatientDetailsEntity updatePatientObj = patientDetailsRepository.save(patientDetailsEntity);
            return PatientDetailsMapper.mapToDto(updatePatientObj);
    }

    @Override
    public void deletePatient(Long patientId) {
        PatientDetailsEntity patientDetailsEntity = patientDetailsRepository.findById(patientId).
                orElseThrow(() -> new ResourceNotFoundException("Patient does not exists with the given ID : " + patientId));
        patientDetailsRepository.deleteById(patientId);

    }




}
