package com.users.microuser.dtos;

import com.users.microuser.enums.Role;
import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private Role role;

}
