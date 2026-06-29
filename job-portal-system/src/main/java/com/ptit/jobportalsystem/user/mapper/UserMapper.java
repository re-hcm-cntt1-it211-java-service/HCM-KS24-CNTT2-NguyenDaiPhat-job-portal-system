package com.ptit.jobportalsystem.user.mapper;

import com.ptit.jobportalsystem.user.dto.request.UpdateProfileRequest;
import com.ptit.jobportalsystem.user.dto.response.UserResponse;
import com.ptit.jobportalsystem.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role.name")
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    void update(UpdateProfileRequest request,
                @MappingTarget User user);
}
