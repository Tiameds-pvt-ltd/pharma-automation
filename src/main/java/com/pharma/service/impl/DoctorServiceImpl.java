package com.pharma.service.impl;

import com.pharma.dto.DoctorDto;
import com.pharma.entity.DoctorEntity;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.DoctorMapper;
import com.pharma.repository.DoctorRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.DoctorService;
import com.pharma.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DoctorDto createDoctor(DoctorDto doctorDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(doctorDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        DoctorEntity doctorEntity = doctorMapper.mapToEntity(doctorDto);
        doctorEntity.setDoctorId(UUID.randomUUID());
        doctorEntity.setCreatedBy(user.getId());
        doctorEntity.setCreatedDate(LocalDate.now());

        doctorEntity.setPharmacyId(doctorDto.getPharmacyId());

        DoctorEntity savedDoctor = doctorRepository.save(doctorEntity);
        return doctorMapper.mapToDto(savedDoctor);
    }

    @Transactional
    @Override
    public List<DoctorDto> getAllDoctors(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<DoctorEntity> doctors = doctorRepository.findAllByPharmacyId(pharmacyId);
        return doctors.stream()
                .map(doctorMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public DoctorDto getDoctorById(Long pharmacyId, UUID doctorId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<DoctorEntity> doctorEntity = doctorRepository.findByDoctorIdAndPharmacyId(doctorId, pharmacyId);

        if (doctorEntity.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId + " for pharmacy ID: " + pharmacyId);
        }
        return doctorMapper.mapToDto(doctorEntity.get());
    }



    @Transactional
    @Override
    public DoctorDto updateDoctor(Long pharmacyId, UUID doctorId, DoctorDto doctorDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<DoctorEntity> doctorEntityOptional = doctorRepository.findByDoctorIdAndPharmacyId(doctorId, pharmacyId);

        if (doctorEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId + " for pharmacy ID: " + pharmacyId);
        }

        DoctorEntity doctorEntity = doctorEntityOptional.get();

        doctorEntity.setDoctorName(doctorDto.getDoctorName());
        doctorEntity.setDoctorSpeciality(doctorDto.getDoctorSpeciality());
        doctorEntity.setDoctorQualification(doctorDto.getDoctorQualification());
        doctorEntity.setDoctorMobile(doctorDto.getDoctorMobile());
        doctorEntity.setDoctorEmail(doctorDto.getDoctorEmail());
        doctorEntity.setDoctorVenue(doctorDto.getDoctorVenue());
        doctorEntity.setDoctorLicenseNumber(doctorDto.getDoctorLicenseNumber());

        doctorEntity.setModifiedBy(user.getId());
        doctorEntity.setModifiedDate(LocalDate.now());

        DoctorEntity updatedDoctor = doctorRepository.save(doctorEntity);
        return doctorMapper.mapToDto(updatedDoctor);
    }


    @Transactional
    @Override
    public void deleteDoctorById(Long pharmacyId, UUID doctorId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<DoctorEntity> doctorEntity = doctorRepository.findByDoctorIdAndPharmacyId(doctorId, pharmacyId);

        if (doctorEntity.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId + " for pharmacy ID: " + pharmacyId);
        }

        doctorRepository.delete(doctorEntity.get());
    }

}
