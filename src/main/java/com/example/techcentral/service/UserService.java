package com.example.techcentral.service;

import com.example.techcentral.ExceptionHandler.ExistException;
import com.example.techcentral.ExceptionHandler.NotFoundException;
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

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new  UsernameNotFoundException(id.toString());
        }
        return new CustomUserDetail(user.get());
    }

    public UserDTO getById (Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("User with id: " +userId + " is not found");
        return UserMapper.TransferToUserDTO(user.get());
    }

    public List<UserDTO> getAllUser(){
        return UserMapper.TransferToUserDTOs(userRepository.findAll());
    }

    public UserDTO createUser(UserRegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new ExistException("user with email: " + request.getEmail()+" have existed");
        }
        User createdUser;
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

        createdUser = userRepository.save(user);

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
           throw new NotFoundException("User with id: " +id+ " is not found");
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
            throw new NotFoundException("User with id: " +id+ " is not found");
        }
        userRepository.deleteById(id);
        return true;
    }

    public UserDTO changeUserRoleToAdmin (Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id: "+userId+"is not found or this user is already a admin");
        }
        user.get().setRole(UserRole.ADMIN);
        User updatedUser = userRepository.save(user.get());
        return UserMapper.TransferToUserDTO(updatedUser);
    }

    public List<UserDTO> getAllAdmin(){
        List<User> adminList = userRepository.findAllByUserRole(UserRole.ADMIN);
        if (adminList.isEmpty())
            throw new NotFoundException("There is no admin");
        return UserMapper.TransferToUserDTOs(adminList);
    }
}
