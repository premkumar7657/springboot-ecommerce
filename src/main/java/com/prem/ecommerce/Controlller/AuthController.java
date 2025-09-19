package com.prem.ecommerce.Controlller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.prem.ecommerce.JWTSecurity.JwtUtils;
import com.prem.ecommerce.JWTSecurity.UserDetailsImpl.UserDetailsImpl;
import com.prem.ecommerce.JWTSecurity.request.LoginRequest;
import com.prem.ecommerce.JWTSecurity.request.SignUpRequest;
import com.prem.ecommerce.JWTSecurity.response.MessageResponse;
import com.prem.ecommerce.JWTSecurity.response.UserInfoResponse;
import com.prem.ecommerce.Model.AppRole;
import com.prem.ecommerce.Model.Roles;
import com.prem.ecommerce.Model.User;
import com.prem.ecommerce.Repository.RoleRepository;
import com.prem.ecommerce.Repository.UserRepository;

import jakarta.validation.Valid;

public class AuthController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user using the provided credentials
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }

        // If authentication is successful, generate a JWT token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(lis -> lis.getAuthority())
                .toList();

        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getUserId(), jwt,
                userDetails.getUsername(), roles);

        return ResponseEntity.ok(userInfoResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Email is already taken!"));
        }

        User user = new User(signUpRequest.getUserName(), passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());

        Set<String> strRoles = signUpRequest.getRole();

        Set<Roles> roles = new HashSet<>();

        if (strRoles == null) {
            Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: No Roles are found"));
            roles.add(userRole);
        } else {
            // admin --> ROLE_ADMIN
            // Seller ==>ROLE_USER

            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                        break;

                    case "seller":

                        Roles adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Roles is not found"));
                        roles.add(adminRole);
                        break;

                    default:
                        Roles defaultRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(defaultRole);
                        break;
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        

    }

}
