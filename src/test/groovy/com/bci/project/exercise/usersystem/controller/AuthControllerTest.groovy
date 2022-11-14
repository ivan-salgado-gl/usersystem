package com.bci.project.exercise.usersystem.controller

import com.bci.project.exercise.usersystem.dto.request.PhoneRegisterDTO
import com.bci.project.exercise.usersystem.dto.request.UserRegisterDTO
import com.bci.project.exercise.usersystem.dto.response.CreatedUserDTO
import com.bci.project.exercise.usersystem.dto.response.PhoneDTO
import com.bci.project.exercise.usersystem.dto.response.UserDTO
import com.bci.project.exercise.usersystem.service.AuthService
import com.bci.project.exercise.usersystem.util.JJWT
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.LocalDateTime

class AuthControllerTest extends Specification {

    private AuthController controller;
    private AuthService authService = Mock(AuthService);
    private JJWT jjwt = Mock(JJWT);

    private final static UUID UID = UUID.fromString("9126a2ee-d991-485d-92ae-48f9e2d9b203");
    private final static LocalDateTime NOW = LocalDateTime.now();
    private final static String TOKEN = "aaa.bbb.ccc";

    def setup() {
        controller = new AuthController();
        controller.authService = this.authService;
        controller.jjwt = this.jjwt;
    }

    def "A user registers and returns code 201"() {
        given: "A new UserRegisterDTO"
        def dto = this.createUserRegisterDTO();
        def createdUserDto = this.getCreatedUserDTO();

        when: "The /signup endpoint is called"
        def response = this.controller.registerUser(dto);

        then: "The user is created successfully"
        1 * this.authService.registerUser(dto) >> {
            createdUserDto
        }

        response.getStatusCode() == HttpStatus.CREATED;
        response.body != null;
        response.body == createdUserDto;
    }

    def "A user login with jwt and returns code 200"() {
        given: "A jwt"
        def email = Optional.of("test@test.com");
        def userDTO = this.createUserDTO();

        when: "The /login endpoint is called"
        def response = this.controller.login(email.get());

        then: "The user is logged in successfully"
        1 * this.jjwt.parseJwtAndGetMail(_) >> { email }
        1 * this.authService.loginUser(email.get()) >> { userDTO }

        response.statusCode == HttpStatus.OK;
        response.body == userDTO;
    }

    private UserRegisterDTO createUserRegisterDTO() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setName("Test");
        userRegisterDTO.setEmail("test@test.com");
        userRegisterDTO.setPassword("12345678");
        userRegisterDTO.setPhones(Arrays.asList(createPhoneRegisterDTO()));
        return userRegisterDTO;
    }

    private PhoneRegisterDTO createPhoneRegisterDTO() {
        PhoneRegisterDTO phoneRegisterDTO = new PhoneRegisterDTO();
        phoneRegisterDTO.setCityCode(221);
        phoneRegisterDTO.setCountryCode("+54");
        phoneRegisterDTO.setNumber(123456);
        return phoneRegisterDTO;
    }

    private CreatedUserDTO getCreatedUserDTO() {
        CreatedUserDTO createdUserDTO = new CreatedUserDTO();
        createdUserDTO.setId(UID);
        createdUserDTO.setIsActive(Boolean.TRUE);
        createdUserDTO.setCreated(NOW);
        createdUserDTO.setLastLogin(null);
        createdUserDTO.setToken(null);
        return createdUserDTO;
    }

    private UserDTO createUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(UID);
        user.setCreated(NOW);
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setPassword("ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f");//ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f
        user.setIsActive(Boolean.TRUE);
        user.setLastLogin(NOW);
        user.setToken(TOKEN)
        user.setPhones(Arrays.asList(this.createPhoneDTO()));
        return user;
    }

    private PhoneDTO createPhoneDTO() {
        PhoneDTO phone = new PhoneDTO();
        phone.setCityCode(221);
        phone.setCountryCode("+54");
        phone.setNumber(123456);
        return phone;
    }

}
