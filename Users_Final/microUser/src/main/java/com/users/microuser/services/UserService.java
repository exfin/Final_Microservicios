package com.users.microuser.services;

import com.users.microuser.dtos.*;
import com.users.microuser.enums.Role;

import java.util.List;

public interface UserService {
    Response<?> register(RegistrationRequest registrationRequest);

    Response<LoginResponse> login(LoginRequest loginRequest);

    Long currentUserId();

    Response<?> updateMyAccount(UserDTO userDTO);

    Response<UserDTO> updateUserById(Long id,UserDTO userDTO);

    Response<List<UserDTO>> findAllUsers();

    Response<?> deleteUser(String email);

    Response<List<UserDTO>> findAllUsersByRole(Role role);

    Response<?> createUserWithRoleDaemon(UserDTO userDTO);
}
