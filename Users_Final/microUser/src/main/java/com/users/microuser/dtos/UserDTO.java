package com.users.microuser.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.users.microuser.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean active;

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
