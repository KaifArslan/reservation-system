package com.kaif.reservation_system.auth;

import com.kaif.reservation_system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private RestaurantUserRepository userRep;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        RestaurantUser user = userRep.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(" User not found"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRestaurantId());

        return ResponseEntity.ok(new LoginResponse(token, user.getRestaurantId()));

    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RestaurantUser request) {
        // Check if email already exists
        if (userRep.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if(request.getName() == null) request.setName(request.getEmail());
        request.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
        userRep.save(request);

        return ResponseEntity.ok("User registered successfully");
    }

}
class LoginRequest {
    private String email;
    private String password;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
class LoginResponse {
    private String token;
    private Integer restaurantId;

    public LoginResponse(String token, Integer restaurantId) {
        this.token = token;
        this.restaurantId = restaurantId;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }
}