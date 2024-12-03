package com.iqbal.sekawan.technical_test.service.impl;

import com.iqbal.sekawan.technical_test.model.User;
import com.iqbal.sekawan.technical_test.repository.IUserRepository;
import com.iqbal.sekawan.technical_test.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    
    private final IUserRepository repository;
    
    @Override
    public User loadUserById(String userId) {
        return repository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED")
        );
    }
}
