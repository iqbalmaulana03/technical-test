package com.iqbal.sekawan.technical_test.service;

import com.iqbal.sekawan.technical_test.dto.request.AuthRequest;
import com.iqbal.sekawan.technical_test.dto.response.RegisterUserResponse;
import com.iqbal.sekawan.technical_test.dto.response.LoginResponse;

public interface IAuthService {

    RegisterUserResponse createCustomer(AuthRequest request);

    LoginResponse login(AuthRequest request);
}
