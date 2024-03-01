package com.example.techcentral.dto.mapper;

import com.example.techcentral.dto.user.UserDTO;
import com.example.techcentral.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {
    public static UserDTO TransferToUserDTO (User user){
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getGender(),
                user.getDob(),
                user.getEmail(),
                user.getPhone(),
                user.getRole());
    }

    public static List<UserDTO> TransferToUserDTOs (List<User> userList){
        return userList.stream().map(user -> TransferToUserDTO(user)).toList();
    }
}
