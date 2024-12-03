package com.iqbal.sekawan.technical_test.controller;

import com.iqbal.sekawan.technical_test.dto.request.AuthRequest;
import com.iqbal.sekawan.technical_test.dto.response.LoginResponse;
import com.iqbal.sekawan.technical_test.dto.response.RegisterUserResponse;
import com.iqbal.sekawan.technical_test.dto.response.WebResponse;
import com.iqbal.sekawan.technical_test.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService service;

    @PostMapping("/register-customer")
    public ResponseEntity<WebResponse<RegisterUserResponse>> registerCustomer(@RequestBody AuthRequest request){
        WebResponse<RegisterUserResponse> response = WebResponse.<RegisterUserResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully created a new customer")
                .data(service.createCustomer(request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<LoginResponse>> login(@RequestBody AuthRequest request){
        LoginResponse login = service.login(request);

        WebResponse<LoginResponse> response = WebResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully login")
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }
}
