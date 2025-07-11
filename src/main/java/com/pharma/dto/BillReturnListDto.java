package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillReturnListDto {

    private UUID billReturnId;
    private String billReturnId1;
    private String billId1;
    private LocalDateTime billReturnDateTime;
    private BigDecimal grandTotal;
    private String patientType;
    private UUID patientId;
    private String patientName;
    private Long returnedItem;

}
