package com.example.akshay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Integer contactId;
    private String name;
    private String email;
    private String mobileNumber;
    private String address;
    private Integer age;
    private String gender;
    private String panCardNumber;
    private String aadhaarCardNumber;
}
