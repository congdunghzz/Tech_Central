package com.example.techcentral.dto;

import com.example.techcentral.models.enums.Gender;
import com.example.techcentral.models.enums.UserRole;


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
