package com.pharma.service;

import com.pharma.dto.DoctorDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    DoctorDto createDoctor(DoctorDto doctorDto, User user);

    List<DoctorDto> getAllDoctors(Long createdById);

    DoctorDto getDoctorById(Long createdById, UUID doctorId);

    DoctorDto updateDoctor(Long modifiedById, UUID doctorId, DoctorDto doctorDto);

    void deleteDoctorById(Long createdById, UUID doctorId);



}
