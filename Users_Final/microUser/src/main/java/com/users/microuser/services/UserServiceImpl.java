package com.users.microuser.services;

import com.users.microuser.Exceptions.NotFoundException;
import com.users.microuser.dao.UserRepo;
import com.users.microuser.dtos.*;
import com.users.microuser.enums.Role;
import com.users.microuser.models.User;
import com.users.microuser.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    public final JwtUtil jwtUtil;


    @Override
    public Response<?> register(RegistrationRequest registrationRequest) {
        if (userRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
            return Response.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("El email ya está registrado")
                    .build();
        }

        User user = new User();
        user.setName(registrationRequest.getName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(Role.SUPER_ADMIN); // Por defecto
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        userRepo.save(user);

        return Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Usuario registrado exitosamente")
                .build();
    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {

        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(user);

        Role role = user.getRole();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRole(role);

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Inicio de sesión exitoso")
                .data(loginResponse)
                .build();
    }

    @Override
    public Long currentUserId() {
        return 0L;
    }

    @Override
    public Response<?> updateMyAccount(UserDTO userDTO) {
        Long userId = userDTO.getId();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        user.setName(userDTO.getName());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cuenta actualizada exitosamente")
                .build();
    }

    @Override
    public Response<UserDTO> updateUserById(Long id, UserDTO userDTO) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        user.setName(userDTO.getName());
        user.setActive(userDTO.isActive());
        user.setRole(userDTO.getRole());
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Usuario actualizado correctamente")
                .data(toDTO(user))
                .build();
    }

    @Override
    public Response<List<UserDTO>> findAllUsers() {
        List<UserDTO> users = userRepo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return Response.<List<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Lista de usuarios obtenida correctamente")
                .data(users)
                .build();
    }

    @Override
    public Response<?> deleteUser(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        userRepo.delete(user);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Usuario eliminado exitosamente")
                .build();
    }

    @Override
    public Response<List<UserDTO>> findAllUsersByRole(Role role) {
        List<UserDTO> users = userRepo.findAll()
                .stream()
                .filter(u -> u.getRole() == role)
                .map(this::toDTO)
                .toList();

        return Response.<List<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Usuarios filtrados por rol")
                .data(users)
                .build();
    }

    @Override
    public Response<?> createUserWithRoleDaemon(UserDTO userDTO) {
        if (userRepo.findByEmail(userDTO.getEmail()).isPresent()) {
            return Response.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("El email ya está registrado")
                    .build();
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.DAEMON);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        userRepo.save(user);

        return Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Usuario DAEMON creado exitosamente")
                .build();
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                null, // no enviamos contraseña
                user.isActive(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }


}
