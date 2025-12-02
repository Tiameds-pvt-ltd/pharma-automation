package com.pharma.service;

import com.pharma.dto.DoctorDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    DoctorDto createDoctor(DoctorDto doctorDto, User user);

    List<DoctorDto> getAllDoctors(Long pharmacyId, User user);

    DoctorDto getDoctorById(Long pharmacyId, UUID doctorId, User user);

    DoctorDto updateDoctor(Long pharmacyId, UUID doctorId, DoctorDto doctorDto, User user);

    void deleteDoctorById(Long pharmacyId, UUID doctorId, User user);



}
