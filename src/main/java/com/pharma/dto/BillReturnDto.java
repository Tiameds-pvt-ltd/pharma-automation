package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillReturnDto {

    private UUID billReturnId;
    private String billReturnId1;
    private UUID pharmacyId;
    private LocalDateTime billReturnDateTime;
    private UUID patientId;
    private UUID doctorId;
    private String doctorName;
    private String patientType;
    private BigDecimal subTotal;
    private BigDecimal totalGst;
    private BigDecimal grandTotal;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

    private List<BillReturnItemDto> billReturnItemDtos = new ArrayList<>();

}
