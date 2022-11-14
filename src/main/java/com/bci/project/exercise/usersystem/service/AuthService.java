package com.bci.project.exercise.usersystem.service;

import com.bci.project.exercise.usersystem.dto.request.UserRegisterDTO;
import com.bci.project.exercise.usersystem.dto.response.CreatedUserDTO;
import com.bci.project.exercise.usersystem.dto.response.UserDTO;
import com.bci.project.exercise.usersystem.exception.ApiException;

public interface AuthService {

    CreatedUserDTO registerUser(final UserRegisterDTO userRegisterDTO) throws ApiException;
    UserDTO loginUser(final String email) throws ApiException;

}
