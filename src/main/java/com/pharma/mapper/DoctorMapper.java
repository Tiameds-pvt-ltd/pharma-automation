package com.pharma.mapper;

import com.pharma.dto.DoctorDto;
import com.pharma.entity.DoctorEntity;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorDto mapToDto(DoctorEntity doctorEntity) {
        return new DoctorDto(
                doctorEntity.getDoctorId(),
                doctorEntity.getDoctorName(),
                doctorEntity.getDoctorSpeciality(),
                doctorEntity.getDoctorQualification(),
                doctorEntity.getDoctorMobile(),
                doctorEntity.getDoctorEmail(),
                doctorEntity.getDoctorVenue(),
                doctorEntity.getDoctorLicenseNumber(),
                doctorEntity.getCreatedBy(),
                doctorEntity.getCreatedDate(),
                doctorEntity.getModifiedBy(),
                doctorEntity.getModifiedDate()

        );
    }

    public static DoctorEntity mapToEntity(DoctorDto doctorDto){
        return new DoctorEntity(
                doctorDto.getDoctorId(),
                doctorDto.getDoctorName(),
                doctorDto.getDoctorSpeciality(),
                doctorDto.getDoctorQualification(),
                doctorDto.getDoctorMobile(),
                doctorDto.getDoctorEmail(),
                doctorDto.getDoctorVenue(),
                doctorDto.getDoctorLicenseNumber(),
                doctorDto.getCreatedBy(),
                doctorDto.getCreatedDate(),
                doctorDto.getModifiedBy(),
                doctorDto.getModifiedDate()
        );
    }

}

