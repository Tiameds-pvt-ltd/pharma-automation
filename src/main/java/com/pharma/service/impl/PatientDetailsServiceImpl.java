package com.pharma.service.impl;


import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.PatientDetailsEntity;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
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
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(patientDetailsDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        PatientDetailsEntity patientDetailsEntity = patientDetailsMapper.mapToEntity(patientDetailsDto);
        patientDetailsEntity.setPatientId(UUID.randomUUID());
        patientDetailsEntity.setCreatedBy(user.getId());
        patientDetailsEntity.setCreatedDate(LocalDate.now());

        patientDetailsEntity.setPharmacyId(patientDetailsDto.getPharmacyId());

        String newPatientId1 = generatePatientId1(patientDetailsDto.getPharmacyId());
        patientDetailsEntity.setPatientId1(newPatientId1);

        PatientDetailsEntity savedPatient = patientDetailsRepository.save(patientDetailsEntity);
        return patientDetailsMapper.mapToDto(savedPatient);
    }

    @Transactional
    @Override
    public List<PatientDetailsDto> getAllPatient(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<PatientDetailsEntity> patientDetailsEntities = patientDetailsRepository.findAllByPharmacyId(pharmacyId);
        return patientDetailsEntities.stream()
                .map(patientDetailsMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PatientDetailsDto getPatientById(Long pharmacyId, UUID patientId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<PatientDetailsEntity> patientDetailsEntity = patientDetailsRepository.findByPatientIdAndPharmacyId(patientId, pharmacyId);

        if (patientDetailsEntity.isEmpty()) {
            throw new ResourceNotFoundException("Patient not found with ID: " + patientId + " for pharmacy ID: " + pharmacyId);
        }
        return patientDetailsMapper.mapToDto(patientDetailsEntity.get());
    }

    @Transactional
    @Override
    public PatientDetailsDto updatePatient(Long pharmacyId, UUID patientId, PatientDetailsDto patientDetailsDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<PatientDetailsEntity> patientEntityOptional = patientDetailsRepository.findByPatientIdAndPharmacyId(patientId, pharmacyId);

        if (patientEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Patient not found with ID: " + patientId +  " for pharmacy ID: " + pharmacyId);
        }

        PatientDetailsEntity patientDetailsEntity = patientEntityOptional.get();

        patientDetailsEntity.setFirstName(patientDetailsDto.getFirstName());
        patientDetailsEntity.setLastName(patientDetailsDto.getLastName());
        patientDetailsEntity.setEmail(patientDetailsDto.getEmail());
        patientDetailsEntity.setPhone(patientDetailsDto.getPhone());
        patientDetailsEntity.setPatientAddress(patientDetailsDto.getPatientAddress());
        patientDetailsEntity.setPatientCity(patientDetailsDto.getPatientCity());
        patientDetailsEntity.setState(patientDetailsDto.getState());
        patientDetailsEntity.setZip(patientDetailsDto.getZip());
        patientDetailsEntity.setBloodGroup(patientDetailsDto.getBloodGroup());
        patientDetailsEntity.setDateOfBirth(patientDetailsDto.getDateOfBirth());
        patientDetailsEntity.setGender(patientDetailsDto.getGender());

        patientDetailsEntity.setModifiedBy(user.getId());
        patientDetailsEntity.setModifiedDate(LocalDate.now());

        PatientDetailsEntity updatedPatient = patientDetailsRepository.save(patientDetailsEntity);
        return patientDetailsMapper.mapToDto(updatedPatient);
    }

    @Transactional
    @Override
    public void deletePatientById(Long pharmacyId, UUID patientId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<PatientDetailsEntity> patientDetailsEntity = patientDetailsRepository.findByPatientIdAndPharmacyId(patientId, pharmacyId);

        if (patientDetailsEntity.isEmpty()) {
            throw new ResourceNotFoundException("Patient not found with ID: " + patientId + " for pharmacy ID: " + pharmacyId);
        }

        patientDetailsRepository.delete(patientDetailsEntity.get());

    }

    @Transactional
    private String generatePatientId1(Long pharmacyId) {

        // YY = last 2 digits of year
        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

        Optional<PatientDetailsEntity> latestPatientOpt =
                patientDetailsRepository.findLatestPatientForYearAndPharmacy(
                        pharmacyId, yearPart
                );

        int nextSequence = 1;

        if (latestPatientOpt.isPresent()) {
            String lastPatientId1 = latestPatientOpt.get().getPatientId1();
            // Example: PAT-25-09 or PAT-25-123
            String[] parts = lastPatientId1.split("-");

            if (parts.length == 3) {
                try {
                    nextSequence = Integer.parseInt(parts[2]) + 1;
                } catch (NumberFormatException ignored) {
                    nextSequence = 1;
                }
            }
        }

        // Pad only for 1–9
        String sequencePart = (nextSequence < 10)
                ? "0" + nextSequence
                : String.valueOf(nextSequence);

        return "PAT-" + yearPart + "-" + sequencePart;
    }

    public String getNextMaxPatientId() {
        String maxId = patientDetailsRepository.findMaxPatientId1();

        if (maxId == null) {
            return "PAT-" + LocalDate.now().getYear() + "-00001";
        }

        String year = maxId.substring(4, 8);
        int number = Integer.parseInt(maxId.substring(9));
        number++;

        return String.format("PAT-%s-%05d", year, number);
    }
}
