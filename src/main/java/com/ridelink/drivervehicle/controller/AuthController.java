package com.ridelink.drivervehicle.controller;

import com.ridelink.drivervehicle.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Fixed token for testing
    private static final String FIXED_TOKEN = "ridelink_test_token_2024";

    @PostMapping("/token")
    public ApiResponse<Map<String, String>> generateToken(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false, defaultValue = "driver") String role) {
        
        if (userId == null || userId.isEmpty()) {
            userId = "default_user";
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("token", FIXED_TOKEN);
        response.put("type", "Bearer");
        response.put("userId", userId);
        response.put("role", role);
        
        return ApiResponse.success("Token generated successfully", response);
    }
}
