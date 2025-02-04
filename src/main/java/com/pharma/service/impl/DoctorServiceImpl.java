package com.pharma.service.impl;

import com.pharma.dto.DoctorDto;
import com.pharma.entity.DoctorEntity;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.DoctorMapper;
import com.pharma.repository.DoctorRepository;
import com.pharma.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;

    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        DoctorEntity doctorEntity = DoctorMapper.mapToEntity(doctorDto);
        DoctorEntity saveDoctor = doctorRepository.save(doctorEntity);
        return DoctorMapper.mapToDto(saveDoctor);
    }

    @Override
    public DoctorDto getDoctorById(Long doctorId) {
        DoctorEntity doctorEntity = doctorRepository.findById(doctorId).
                orElseThrow(() -> new ResourceNotFoundException("Patient does not exists with the given ID : " + doctorId));
        return DoctorMapper.mapToDto(doctorEntity);
    }

    @Override
    public List<DoctorDto> getAllDoctor() {
        List<DoctorEntity> doctorEntities = doctorRepository.findAll();
        return doctorEntities.stream().map((doctorEntity) -> DoctorMapper.mapToDto(doctorEntity)).collect(Collectors.toList());
    }

    @Override
    public DoctorDto updateDoctor(Long doctorId, DoctorDto updateDoctor) {
        DoctorEntity doctorEntity = doctorRepository.findById(doctorId).
                orElseThrow(() -> new ResourceNotFoundException("Patient does not exists with the given ID : " + doctorId));

        doctorEntity.setDoctorInitial(updateDoctor.getDoctorInitial());
        doctorEntity.setDoctorName(updateDoctor.getDoctorName());
        doctorEntity.setDoctorSpeciality(updateDoctor.getDoctorSpeciality());
        doctorEntity.setDoctorQualification(updateDoctor.getDoctorQualification());
        doctorEntity.setDoctorMobile(updateDoctor.getDoctorMobile());
        doctorEntity.setDoctorVenue(updateDoctor.getDoctorVenue());

        DoctorEntity updateDoctorObj = doctorRepository.save(doctorEntity);
        return DoctorMapper.mapToDto(updateDoctorObj);
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        DoctorEntity doctorEntity = doctorRepository.findById(doctorId).
                orElseThrow(() -> new ResourceNotFoundException("Patient does not exists with the given ID : " + doctorId));
        doctorRepository.deleteById(doctorId);

    }
}
