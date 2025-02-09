package com.iqbal.sekawan.technical_test.repository;

import com.iqbal.sekawan.technical_test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}
