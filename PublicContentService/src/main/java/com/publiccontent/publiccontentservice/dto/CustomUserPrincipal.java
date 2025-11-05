package com.publiccontent.publiccontentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomUserPrincipal {
    private Long id;
    private String email;
    private String role;
}