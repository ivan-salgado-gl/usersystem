package com.bci.project.exercise.usersystem.service

import com.bci.project.exercise.usersystem.dto.request.PhoneRegisterDTO
import com.bci.project.exercise.usersystem.dto.request.UserRegisterDTO
import com.bci.project.exercise.usersystem.dto.response.CreatedUserDTO
import com.bci.project.exercise.usersystem.dto.response.PhoneDTO
import com.bci.project.exercise.usersystem.dto.response.UserDTO
import com.bci.project.exercise.usersystem.entity.Phone
import com.bci.project.exercise.usersystem.entity.User
import com.bci.project.exercise.usersystem.enums.State
import com.bci.project.exercise.usersystem.repository.UserRepository
import com.bci.project.exercise.usersystem.service.impl.AuthServiceImpl
import com.bci.project.exercise.usersystem.util.JJWT
import spock.lang.Specification

import java.time.LocalDateTime

class AuthServiceImplTest extends Specification {

    private AuthServiceImpl service;
    private UserRepository userRepository = Mock(UserRepository);
    private JJWT jjwt = Mock(JJWT);

    private final static UUID UID = UUID.fromString("9126a2ee-d991-485d-92ae-48f9e2d9b203");
    private final static LocalDateTime NOW = LocalDateTime.now();
    private final static String TOKEN = "aaa.bbb.ccc";

    def setup() {
        service = new AuthServiceImpl();
        service.userRepository = this.userRepository;
        service.jjwt = this.jjwt;
    }

    def "A user is created"() {
        given: "The information of a user to be registered"
        def dto = this.createUserRegisterDTO();
        def user = this.createUser();
        def newUser = this.getCreatedUserDTO();

        when: "The service to register the user is executed"
        def response = this.service.registerUser(dto);

        then: "The user is created"
        1 * this.userRepository.existsByEmail(dto.getEmail()) >> { false }
        1 * this.userRepository.save(_) >> { user }
        1 * this.jjwt.generateToken(_) >> { TOKEN }

        response == newUser;
    }

    def "A user login"() {
        given: "The information of a user to login"
        def email = "test@test.com";
        def user = Optional.of(this.createUser());
        def userDto = this.createUserDTO();

        when: "The service to login is executed"
        def response = this.service.loginUser(email);

        then: "Get user information"
        1 * this.userRepository.findByEmail(email) >> { user }
        1 * this.userRepository.save(_) >> { user.get() }
        1 * this.jjwt.generateToken(_) >> { TOKEN }

        userDto.setLastLogin(response.getLastLogin());

        response == userDto;
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

    private User createUser() {
        User user = new User();
        user.setId(UID);
        user.setCreated(NOW);
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setPassword("ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f");//ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f
        user.setState(State.ENABLED);
        user.setPhones(Arrays.asList(this.createPhone()));
        return user;
    }

    private Phone createPhone() {
        Phone phone = new Phone();
        phone.setCityCode(221);
        phone.setCountryCode("+54");
        phone.setNumber(123456);
        return phone;
    }

    private CreatedUserDTO getCreatedUserDTO() {
        CreatedUserDTO createdUserDTO = new CreatedUserDTO();
        createdUserDTO.setId(UID);
        createdUserDTO.setIsActive(Boolean.TRUE);
        createdUserDTO.setCreated(NOW);
        createdUserDTO.setLastLogin(null);
        createdUserDTO.setToken(TOKEN);
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
