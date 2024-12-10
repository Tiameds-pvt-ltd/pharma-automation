package com.pharma.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {
    private Long id;
    private String name;
//    private String description; // Optional, based on your requirement
}