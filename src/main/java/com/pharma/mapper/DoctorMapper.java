package com.pharma.mapper;

import com.pharma.dto.DoctorDto;
import com.pharma.entity.DoctorEntity;

public class DoctorMapper {

    public static DoctorDto mapToDto(DoctorEntity doctorEntity) {
        return new DoctorDto(
                doctorEntity.getDoctorId(),
                doctorEntity.getDoctorInitial(),
                doctorEntity.getDoctorName(),
                doctorEntity.getDoctorSpeciality(),
                doctorEntity.getDoctorQualification(),
                doctorEntity.getDoctorMobile(),
                doctorEntity.getDoctorVenue(),
                doctorEntity.getDoctorEnteredDate(),
                doctorEntity.getDoctorEnteredBy()

        );
    }


    public static DoctorEntity mapToEntity(DoctorDto doctorDto){
        return new DoctorEntity(
                doctorDto.getDoctorId(),
                doctorDto.getDoctorInitial(),
                doctorDto.getDoctorName(),
                doctorDto.getDoctorSpeciality(),
                doctorDto.getDoctorQualification(),
                doctorDto.getDoctorMobile(),
                doctorDto.getDoctorVenue(),
                doctorDto.getDoctorEnteredDate(),
                doctorDto.getDoctorEnteredBy()
        );
    }


}

