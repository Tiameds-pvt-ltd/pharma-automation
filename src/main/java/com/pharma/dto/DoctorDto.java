package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {

    private Long doctorId;
    private String doctorInitial;
    private String doctorName;
    private String doctorSpeciality;
    private String doctorQualification;
    private Long doctorMobile;
    private String doctorVenue;
    private LocalDate doctorEnteredDate;
    private Long doctorEnteredBy;

}
