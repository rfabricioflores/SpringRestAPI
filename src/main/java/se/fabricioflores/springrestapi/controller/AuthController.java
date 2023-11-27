package se.fabricioflores.springrestapi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.fabricioflores.springrestapi.config.JwtUtil;
import se.fabricioflores.springrestapi.dto.UserRegisterReq;
import se.fabricioflores.springrestapi.model.User;
import se.fabricioflores.springrestapi.service.IUserService;

@RestController
@RequestMapping({"/auth", "/auth/"})
public class AuthController {

    private final IUserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(
            IUserService userService,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping({"/register", "/register/"})
    public ResponseEntity<Object> register(@RequestBody UserRegisterReq userRegisterReq) {
        User user = userService.registerUser(userRegisterReq);
        return ResponseEntity.ok("User " + user.getUsername() + " registered successfully");
    }

    @PostMapping({"/login", "/login/"})
    public ResponseEntity<Object> login(@RequestBody @Valid UserRegisterReq userRegisterReq) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRegisterReq.username(), userRegisterReq.password())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            String token = jwtUtil.generateToken(SecurityContextHolder.getContext().getAuthentication());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(401).body("Login failed");
        }
    }

    @GetMapping("/welcome")
    public ResponseEntity<Object> welcome() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok("Welcome " + username);
    }
}
