package com.bci.project.exercise.usersystem.service.impl;

import com.bci.project.exercise.usersystem.dto.request.UserRegisterDTO;
import com.bci.project.exercise.usersystem.dto.response.CreatedUserDTO;
import com.bci.project.exercise.usersystem.dto.response.UserDTO;
import com.bci.project.exercise.usersystem.entity.User;
import com.bci.project.exercise.usersystem.enums.CodeType;
import com.bci.project.exercise.usersystem.exception.ApiException;
import com.bci.project.exercise.usersystem.mapper.UserMapper;
import com.bci.project.exercise.usersystem.repository.UserRepository;
import com.bci.project.exercise.usersystem.service.AuthService;
import com.bci.project.exercise.usersystem.util.ErrorManager;
import com.bci.project.exercise.usersystem.util.JJWT;
import com.bci.project.exercise.usersystem.util.PasswordManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JJWT jjwt;

    @Override
    public CreatedUserDTO registerUser(final UserRegisterDTO userRegisterDTO) throws ApiException {
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            log.error("User already exists.");
            throw ErrorManager.createError(CodeType.EXISTING_RECORD);
        }

        final User user = UserMapper.INSTANCE.userRegisterDtoToUser(userRegisterDTO);
        user.setPassword(PasswordManager.encrypt(user.getPassword()));

        User newUser;
        try {
            newUser = this.userRepository.save(user);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw ErrorManager.createError(CodeType.GENERIC_ERROR);
        }

        final String jwt = this.jjwt.generateToken(user);
        return UserMapper.INSTANCE.userToCreatedUserDto(newUser, jwt);
    }

    @Override
    public UserDTO loginUser(final String email) throws ApiException {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> ErrorManager.createError(CodeType.RECORD_NOT_FOUND));

        user = this.updateLoginTime(user);

        final String jwt = this.jjwt.generateToken(user);
        return UserMapper.INSTANCE.userToUserDto(user, jwt);
    }

    private User updateLoginTime(final User user) throws ApiException {
        try {
            user.setLastLogin(LocalDateTime.now());
            return this.userRepository.save(user);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw ErrorManager.createError(CodeType.GENERIC_ERROR);
        }
    }

}
