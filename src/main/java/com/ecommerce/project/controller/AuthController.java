package com.ecommerce.project.controller;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.CustomUserDetails;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.RegisterRequest;
import com.ecommerce.project.security.response.LoginResponse;
import com.ecommerce.project.security.response.MessageResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor(onConstructor_= {@Autowired})
public class AuthController {

    private final RoleRepository roleRepository;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @PostMapping("sign-in")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Bad Credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        String token = jwtUtils.generateToken(userDetails);
        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(userDetails.getUser().getUserId());
        loginResponse.setUsername(userDetails.getUsername());
//        loginResponse.setJwtToken(token);
        loginResponse.setRoles(roles);

//        return ResponseEntity.ok(loginResponse);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
        }

        Set<Role> roles = new HashSet<>();
        Set<String> requestedRoles = registerRequest.getRoles();
        if (requestedRoles == null || requestedRoles.isEmpty()) {
            Role defaultRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role does not exist"));
            roles.add(defaultRole);
        } else {
            for (String role : requestedRoles) {
                String roleName = "ROLE_" + role.toUpperCase();
                Role fetchedRole = roleRepository.findByRoleName(AppRole.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                roles.add(fetchedRole);
            }
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setHashedPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/username")
    public String currentUsername(Authentication authentication) {
        return authentication == null ? "" : authentication.getName();
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        CustomUserDetails userDetails = authentication != null ? (CustomUserDetails) authentication.getPrincipal() : null;
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        LoginResponse response = LoginResponse.builder()
                .id(userDetails.getUser().getUserId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
        return ResponseEntity.ok(response);

    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(Authentication authentication) {
        ResponseCookie cookie = jwtUtils.generateCleanJwtCookie();
        MessageResponse response = new MessageResponse("You've been signed out!");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }
}
