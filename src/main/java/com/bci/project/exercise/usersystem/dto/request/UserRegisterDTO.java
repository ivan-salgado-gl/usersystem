package com.bci.project.exercise.usersystem.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserRegisterDTO {

    private String name;

    @NotEmpty(message = "The email address is required.")
    @Email(message = "he email address is invalid.")
    private String email;

    @NotEmpty(message = "The password is required.")
    @Size(min = 8, max = 12, message = "The password must be between 8 and 12 characters.")
    private String password;

    @Valid
    private List<PhoneRegisterDTO> phones;

}
