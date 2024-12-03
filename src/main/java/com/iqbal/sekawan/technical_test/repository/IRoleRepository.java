package com.iqbal.sekawan.technical_test.repository;

import com.iqbal.sekawan.technical_test.model.Roles;
import com.iqbal.sekawan.technical_test.statval.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByPart(ERole role);
}
