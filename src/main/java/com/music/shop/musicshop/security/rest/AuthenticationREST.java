package com.music.shop.musicshop.security.rest;

import com.music.shop.musicshop.security.JWTUtil;
import com.music.shop.musicshop.security.PBKDF2Encoder;
import com.music.shop.musicshop.security.model.AuthRequest;
import com.music.shop.musicshop.security.model.AuthResponse;
import com.music.shop.musicshop.security.model.Role;
import com.music.shop.musicshop.security.model.User;
import com.music.shop.musicshop.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/security")

public class AuthenticationREST {
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername()).map((userDetails) -> {
            if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    @PostMapping("/registration")
    public Mono<User> userRegistration(@RequestBody User user) {
        System.out.println(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRoleToList(Role.ROLE_USER);
        user.setEnabled(true);
        Mono<User> userMono = userService.saveUser(user);
        userMono.subscribe(s -> System.out.println(s));
        System.out.println(user);
        return Mono.just(user);
    }

}
