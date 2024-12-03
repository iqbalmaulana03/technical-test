package com.iqbal.sekawan.technical_test.service;

import com.iqbal.sekawan.technical_test.model.User;

public interface IUserService {

    User loadUserById(String userId);
}
