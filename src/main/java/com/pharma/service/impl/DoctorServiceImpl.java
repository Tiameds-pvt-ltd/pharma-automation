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
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        DoctorEntity doctorEntity = doctorMapper.mapToEntity(doctorDto);
        doctorEntity.setDoctorId(UUID.randomUUID());
        doctorEntity.setCreatedBy(user.getId());
        doctorEntity.setCreatedDate(LocalDate.now());

        DoctorEntity savedDoctor = doctorRepository.save(doctorEntity);
        return doctorMapper.mapToDto(savedDoctor);
    }

    @Transactional
    @Override
    public List<DoctorDto> getAllDoctors(Long createdById) {
        List<DoctorEntity> doctors = doctorRepository.findAllByCreatedBy(createdById);
        return doctors.stream()
                .map(doctorMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public DoctorDto getDoctorById(Long createdById, UUID doctorId) {
        Optional<DoctorEntity> doctorEntity = doctorRepository.findByDoctorIdAndCreatedBy(doctorId, createdById);

        if (doctorEntity.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId + " for user ID: " + createdById);
        }
        return doctorMapper.mapToDto(doctorEntity.get());
    }



    @Transactional
    @Override
    public DoctorDto updateDoctor(Long modifiedById, UUID doctorId, DoctorDto doctorDto) {
        Optional<DoctorEntity> doctorEntityOptional = doctorRepository.findById(doctorId);

        if (doctorEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }

        DoctorEntity doctorEntity = doctorEntityOptional.get();

        doctorEntity.setDoctorName(doctorDto.getDoctorName());
        doctorEntity.setDoctorSpeciality(doctorDto.getDoctorSpeciality());
        doctorEntity.setDoctorQualification(doctorDto.getDoctorQualification());
        doctorEntity.setDoctorEmail(doctorDto.getDoctorEmail());
        doctorEntity.setDoctorVenue(doctorDto.getDoctorVenue());

        doctorEntity.setModifiedBy(modifiedById);
        doctorEntity.setModifiedDate(LocalDate.now());

        DoctorEntity updatedDoctor = doctorRepository.save(doctorEntity);
        return doctorMapper.mapToDto(updatedDoctor);
    }


    @Transactional
    @Override
    public void deleteDoctorById(Long createdById, UUID doctorId) {
        Optional<DoctorEntity> doctorEntity = doctorRepository.findByDoctorIdAndCreatedBy(doctorId, createdById);

        if (doctorEntity.isEmpty()) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId + " for user ID: " + createdById);
        }

        doctorRepository.delete(doctorEntity.get());
    }

}
