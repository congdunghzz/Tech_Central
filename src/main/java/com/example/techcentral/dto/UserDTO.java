package com.example.techcentral.dto;

import com.example.techcentral.enums.Gender;
import com.example.techcentral.enums.UserRole;


import java.sql.Date;

public record UserDTO(
         Long id,
         String name,
         Gender gender,
         Date dob,
         String email,
         String phone,
         UserRole role,
         String password
) {
}
