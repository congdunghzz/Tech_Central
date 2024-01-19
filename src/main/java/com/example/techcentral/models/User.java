package com.example.techcentral.models;

import com.example.techcentral.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GENDER")
    private boolean gender;

    @Column(name = "DOB")
    private Date dob;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "USER_NAME")
    private String user_name;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}

