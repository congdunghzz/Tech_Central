package com.example.techcentral.controller;

import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.example.techcentral.ExceptionHandler.UnAuthorizedException;
import com.example.techcentral.dto.user.CustomUserDetail;
import com.example.techcentral.dto.user.UserDTO;
import com.example.techcentral.dto.user.UserRegisterRequest;
import com.example.techcentral.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getALlUser (){
        List<UserDTO> userDTOS = userService.getAllUser();
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById (@PathVariable Long id){
        UserDTO userDTO = userService.getById(id);

        if (userDTO != null){
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/myProfile")
    public ResponseEntity<UserDTO> getMyProfile (
            @CurrentSecurityContext(expression="authentication") Authentication authentication){

        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
            userId = user.getUser().getId();

        }

        UserDTO userDTO;
        if (userId != null){
            userDTO = userService.getById(userId);
        }else {
            throw new NotFoundException("Not login");
        }

        if (userDTO != null){
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }



    @PutMapping("/myProfile")
    public ResponseEntity<UserDTO> editInfo(
            @CurrentSecurityContext(expression="authentication") Authentication authentication,
            @RequestBody UserRegisterRequest request){
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
            userId = user.getUser().getId();

        }
        UserDTO updatedUser;
        if (userId != null){
            updatedUser = userService.editInfo(userId, request);
        }else {
            throw new NotFoundException("Not login");
        }

        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteUser(@PathVariable Long id){
        Map<String,String> result = new HashMap<>();
        HttpStatus status;
        if (userService.deleteUser(id)){
            result.put("message", "deleted");
            status = HttpStatus.ACCEPTED;
        }else {
            result.put("message", "some thing went wrong");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(result, status);
    }

    
    @PutMapping("/admin/{id}")
    public ResponseEntity<UserDTO> updateToAdminRole (@PathVariable Long id){
        UserDTO userDto = userService.changeUserRoleToAdmin(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserDTO>> getAllAdmin(){
        List<UserDTO> adminList = userService.getAllAdmin();
        return ResponseEntity.ok().body(adminList);
    }
}
