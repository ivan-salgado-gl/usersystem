package com.bci.project.exercise.usersystem.mapper;

import com.bci.project.exercise.usersystem.dto.request.UserRegisterDTO;
import com.bci.project.exercise.usersystem.dto.response.CreatedUserDTO;
import com.bci.project.exercise.usersystem.dto.response.UserDTO;
import com.bci.project.exercise.usersystem.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(
            target = "state",
            expression = "java( com.bci.project.exercise.usersystem.enums.State.ENABLED )"
    )
    User userRegisterDtoToUser(final UserRegisterDTO userRegisterDTO);

    @AfterMapping
    default void afterUserMapping(@MappingTarget final User user) {
        if (user.getPhones() != null) {
            user.getPhones().forEach(phone -> phone.setUser(user));
        }
    }

    @Mapping(
            target = "isActive",
            expression = "java( new Boolean(user.getState().equals(com.bci.project.exercise.usersystem.enums.State.ENABLED) ? true : false) )"
    )
    @Mapping(
            source = "jwt",
            target = "token"
    )
    CreatedUserDTO userToCreatedUserDto(final User user, final String jwt);

    @Mapping(
            target = "isActive",
            expression = "java( new Boolean(user.getState().equals(com.bci.project.exercise.usersystem.enums.State.ENABLED) ? true : false) )"
    )
    @Mapping(
            source = "jwt",
            target = "token"
    )
    UserDTO userToUserDto(final User user, final String jwt);

}
