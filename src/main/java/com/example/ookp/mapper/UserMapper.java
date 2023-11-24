package com.example.ookp.mapper;

import com.example.ookp.dto.UserDTO;
import com.example.ookp.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDTO userDTO);
    UserDTO toUserDTO(User user);
}
