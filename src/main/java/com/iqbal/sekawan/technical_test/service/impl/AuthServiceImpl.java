package com.iqbal.sekawan.technical_test.service.impl;

import com.iqbal.sekawan.technical_test.dto.request.AuthRequest;
import com.iqbal.sekawan.technical_test.dto.response.LoginResponse;
import com.iqbal.sekawan.technical_test.dto.response.RegisterUserResponse;
import com.iqbal.sekawan.technical_test.model.Roles;
import com.iqbal.sekawan.technical_test.model.User;
import com.iqbal.sekawan.technical_test.repository.IUserRepository;
import com.iqbal.sekawan.technical_test.service.IAuthService;
import com.iqbal.sekawan.technical_test.service.IRoleService;
import com.iqbal.sekawan.technical_test.statval.ERole;
import com.iqbal.sekawan.technical_test.utils.JwtUtils;
import com.iqbal.sekawan.technical_test.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ValidationUtils validationUtils;
    private final IRoleService roleService;

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public void initSuperAdmin(){

        String emailSuper = "superadmin@gmail.com";

        Optional<User> optionalUser = repository.findByEmail(emailSuper);

        if (optionalUser.isPresent()) return;

        Roles roleSuperAdmin = roleService.getOrSave(ERole.ROLE_SUPER_ADMIN);

        Roles roleAdmin = roleService.getOrSave(ERole.ROLE_ADMIN);

        String encode = passwordEncoder.encode("P@ssword9898");

        User user = User.builder()
                .email(emailSuper)
                .password(encode)
                .parts(List.of(roleSuperAdmin, roleAdmin))
                .build();
        repository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterUserResponse createCustomer(AuthRequest request) {

        validationUtils.validate(request);

        Roles roleCustomer = roleService.getOrSave(ERole.ROLE_CUSTOMER);

        String encode = passwordEncoder.encode(request.password());

        User user = User.builder()
                .email(request.email())
                .password(encode)
                .parts(List.of(roleCustomer))
                .build();

        repository.saveAndFlush(user);
        
        return toRegisterResponse(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(AuthRequest request) {

        validationUtils.validate(request);

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String token = jwtUtils.generateToken(user);
        List<String> parts = user.getParts().stream().map(part -> part.getPart().name()).toList();

        return LoginResponse.builder()
                .token(token)
                .parts(parts)
                .build();
    }

    private RegisterUserResponse toRegisterResponse(User user){
        List<String> parts = user.getParts().stream().map(part -> part.getPart().name()).toList();

        return RegisterUserResponse.builder()
                .email(user.getEmail())
                .roles(parts)
                .build();
    }
}
