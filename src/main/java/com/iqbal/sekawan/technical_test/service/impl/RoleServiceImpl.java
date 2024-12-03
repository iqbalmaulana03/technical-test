package com.iqbal.sekawan.technical_test.service.impl;

import com.iqbal.sekawan.technical_test.model.Roles;
import com.iqbal.sekawan.technical_test.repository.IRoleRepository;
import com.iqbal.sekawan.technical_test.service.IRoleService;
import com.iqbal.sekawan.technical_test.statval.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Roles getOrSave(ERole role) {

        Optional<Roles> optionalRole = repository.findByPart(role);

        if (optionalRole.isPresent()) return optionalRole.get();

        Roles roles = Roles.builder()
                .part(role)
                .build();

        return repository.save(roles);
    }
}
