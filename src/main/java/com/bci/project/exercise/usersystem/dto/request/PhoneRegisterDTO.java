package com.bci.project.exercise.usersystem.dto.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class PhoneRegisterDTO {

    private Long number;

    private Integer cityCode;

    @Size(min = 1, max = 8, message = "The country code must be between 1 and 8 characters.")
    private String countryCode;

}
