package com.prem.ecommerce.JWTSecurity.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignUpRequest {

    private Long id;

    @NotBlank
    @Size(min = 3, max=20)
    private String userName;

    @NotBlank
    @Size(max=50)
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max=40)
    private String password;
    



}
