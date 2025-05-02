package com.pharma.service.impl;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.DoctorEntity;
import com.pharma.entity.PatientDetailsEntity;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.DoctorMapper;
import com.pharma.mapper.PatientDetailsMapper;
import com.pharma.repository.PatientDetailsRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PatientDetailsService;
import com.pharma.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientDetailsServiceImpl implements PatientDetailsService {

    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

    @Autowired
    private PatientDetailsMapper patientDetailsMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public PatientDetailsDto createPatient(PatientDetailsDto patientDetailsDto, User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PatientDetailsEntity patientDetailsEntity = patientDetailsMapper.mapToEntity(patientDetailsDto);
        patientDetailsEntity.setPatientId(UUID.randomUUID());
        patientDetailsEntity.setCreatedBy(user.getId());
        patientDetailsEntity.setCreatedDate(LocalDate.now());

        PatientDetailsEntity savedPatient = patientDetailsRepository.save(patientDetailsEntity);
        return patientDetailsMapper.mapToDto(savedPatient);
    }

    @Transactional
    @Override
    public List<PatientDetailsDto> getAllPatient(Long createdById) {
        List<PatientDetailsEntity> patientDetailsEntities = patientDetailsRepository.findAllByCreatedBy(createdById);
        return patientDetailsEntities.stream()
                .map(patientDetailsMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PatientDetailsDto getPatientById(Long createdById, UUID patientId) {
        Optional<PatientDetailsEntity> patientDetailsEntity = patientDetailsRepository.findByPatientIdAndCreatedBy(patientId, createdById);

        if (patientDetailsEntity.isEmpty()) {
            throw new ResourceNotFoundException("Patient not found with ID: " + patientId + " for user ID: " + createdById);
        }
        return patientDetailsMapper.mapToDto(patientDetailsEntity.get());
    }

    @Transactional
    @Override
    public PatientDetailsDto updatePatient(Long modifiedById, UUID patientId, PatientDetailsDto patientDetailsDto) {
        Optional<PatientDetailsEntity> patientEntityOptional = patientDetailsRepository.findById(patientId);

        if (patientEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Patient not found with ID: " + patientId);
        }

        PatientDetailsEntity patientDetailsEntity = patientEntityOptional.get();

        patientDetailsEntity.setPatientName(patientDetailsDto.getPatientName());
        patientDetailsEntity.setPatientNumber(patientDetailsDto.getPatientNumber());
        patientDetailsEntity.setPatientMobile(patientDetailsDto.getPatientMobile());
        patientDetailsEntity.setPatientEmail(patientDetailsDto.getPatientEmail());
        patientDetailsEntity.setPatientAddress(patientDetailsDto.getPatientAddress());

        patientDetailsEntity.setModifiedBy(modifiedById);
        patientDetailsEntity.setModifiedDate(LocalDate.now());

        PatientDetailsEntity updatedPatient = patientDetailsRepository.save(patientDetailsEntity);
        return patientDetailsMapper.mapToDto(updatedPatient);
    }

    @Transactional
    @Override
    public void deletePatientById(Long createdById, UUID patientId) {

        Optional<PatientDetailsEntity> patientDetailsEntity = patientDetailsRepository.findByPatientIdAndCreatedBy(patientId, createdById);

        if (patientDetailsEntity.isEmpty()) {
            throw new ResourceNotFoundException("Patient not found with ID: " + patientId + " for user ID: " + createdById);
        }

        patientDetailsRepository.delete(patientDetailsEntity.get());

    }
}
