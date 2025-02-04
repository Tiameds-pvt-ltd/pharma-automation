package com.pharma.service;

import com.pharma.dto.DoctorDto;

import java.util.List;

public interface DoctorService {

    DoctorDto createDoctor(DoctorDto doctorDto);

    DoctorDto getDoctorById(Long doctorId);

    List<DoctorDto> getAllDoctor();

    DoctorDto updateDoctor(Long doctorId, DoctorDto updateDoctor);

    void deleteDoctor(Long doctorId);

}
