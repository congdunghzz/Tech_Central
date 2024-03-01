package com.example.techcentral.service;

import com.example.techcentral.dao.UserRepository;
import com.example.techcentral.dto.user.CustomUserDetail;
import com.example.techcentral.dto.user.UserDTO;
import com.example.techcentral.dto.user.UserRegisterRequest;
import com.example.techcentral.dto.mapper.UserMapper;
import com.example.techcentral.enums.UserRole;
import com.example.techcentral.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new  UsernameNotFoundException(email);
        }
        return new CustomUserDetail(user.get());
    }

    public UserDTO getById (Long userId){
        Optional<User> user = userRepository.findById(userId);
        return user.map(UserMapper::TransferToUserDTO).orElse(null);
    }

    public List<UserDTO> getAllUser(){
        return UserMapper.TransferToUserDTOs(userRepository.findAll());
    }

    public UserDTO createUser(UserRegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            return null;
        }
        User user = User
                .builder()
                .name(request.getName())
                .dob(request.getDob())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();
        try{
            userRepository.save(user);
        }catch (DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("data is duplicated");
        }
        User createdUser = userRepository.findByEmail(user.getEmail()).get();
        return UserMapper.TransferToUserDTO(createdUser);
    }

    public Long getIdByEmail(String email){
            Optional<Long> userId = userRepository.findIdByEmail(email);
            if (userId.isEmpty()){
                throw new  UsernameNotFoundException(email);
            }
            return userId.get();
    }

    public UserDTO editInfo(Long id,UserRegisterRequest request){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            System.out.println("User Service: User is not be found");
            return null;
        }

        user.get().setName(request.getName());
        user.get().setDob(request.getDob());
        user.get().setGender(request.getGender());
        user.get().setPhone(request.getPhone());

        User updatedUser = userRepository.save(user.get());

        return UserMapper.TransferToUserDTO(updatedUser);
    }

    public boolean deleteUser(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            System.out.println("User Service: User is not be found");
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}
