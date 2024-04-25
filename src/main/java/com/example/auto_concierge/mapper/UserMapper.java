package com.example.auto_concierge.mapper;

import com.example.auto_concierge.dto.user.UserRegistrationFormData;
import com.example.auto_concierge.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    @Mapping(source = "role", target = "role")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User registrationFormDataToUser(UserRegistrationFormData formData);

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
