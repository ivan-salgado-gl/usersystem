package com.bci.project.exercise.usersystem.controller;

import com.bci.project.exercise.usersystem.dto.error.ResponseDTO;
import com.bci.project.exercise.usersystem.dto.request.UserRegisterDTO;
import com.bci.project.exercise.usersystem.dto.response.CreatedUserDTO;
import com.bci.project.exercise.usersystem.dto.response.UserDTO;
import com.bci.project.exercise.usersystem.enums.CodeType;
import com.bci.project.exercise.usersystem.exception.ApiException;
import com.bci.project.exercise.usersystem.service.AuthService;
import com.bci.project.exercise.usersystem.util.ErrorManager;
import com.bci.project.exercise.usersystem.util.JJWT;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JJWT jjwt;

    @ApiOperation(value = "Sign-in user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user logged in successfully", response = UserDTO.class),
            @ApiResponse(code = 401, message = "Unauthorized user", response = ResponseDTO.class),
            @ApiResponse(code = 404, message = "User not found", response = ResponseDTO.class),
            @ApiResponse(code = 500, message = "Generic error", response = ResponseDTO.class)
    })
    @RequestMapping(
            value = "/login",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDTO> login(@RequestHeader("token") final String jwt) throws ApiException {
        log.info("User login.");
        Optional<String> email = this.jjwt.parseJwtAndGetMail(jwt);

        if (email.isPresent()) {
            final UserDTO userDTO = this.authService.loginUser(email.get());
            return ResponseEntity.ok(userDTO);
        } else {
            log.error("email not found.");
            throw ErrorManager.createError(CodeType.ILEGAL_JWT_CLAIMS);
        }

    }

    @ApiOperation(value = "Sign-up user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The user was created successfully", response = CreatedUserDTO.class),
            @ApiResponse(code = 409, message = "User already exists", response = ResponseDTO.class),
            @ApiResponse(code = 500, message = "Generic error", response = ResponseDTO.class)
    })
    @RequestMapping(
            value = "/signup",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CreatedUserDTO> registerUser(@Valid @RequestBody final UserRegisterDTO userRegisterDTO) throws ApiException {
        log.info("User register.");
        final CreatedUserDTO user = this.authService.registerUser(userRegisterDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
