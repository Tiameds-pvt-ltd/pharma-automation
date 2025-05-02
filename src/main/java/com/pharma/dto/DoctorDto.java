package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {

    private UUID doctorId;
    private String doctorName;
    private String doctorSpeciality;
    private String doctorQualification;
    private Long doctorMobile;
    private String doctorEmail;
    private String doctorVenue;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;
}
