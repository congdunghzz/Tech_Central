package com.example.techcentral.dto.user;

import com.example.techcentral.enums.Gender;
import com.example.techcentral.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.sql.Date;

public record UserDTO(
         Long id,
         String name,
         Gender gender,
         Date dob,
         String email,
         String phone,
         UserRole role
) {
}
