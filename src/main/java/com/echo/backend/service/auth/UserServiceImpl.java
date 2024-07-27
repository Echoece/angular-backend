package com.echo.backend.service.auth;

import com.echo.backend.config.security.JwtService;
import com.echo.backend.entity.Users;
import com.echo.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static com.echo.backend.utility.Utility.copyNonNullProperties;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;


    @Override
    public Users save(Users payload) {
        payload.setPassword(encoder.encode(payload.getPassword()));
        Users savedUser = userRepository.save(payload);

        savedUser.setToken(buildToken(savedUser));
        return savedUser;
    }


    @Override
    public Users update(Users payload, Long id) {
        findById(id);

        payload.setId(id);
        if (Objects.nonNull(payload.getPassword()))
            payload.setPassword(encoder.encode(payload.getPassword()));


        return userRepository.save(payload);
    }

    @Override
    public Users updatePartial(Users payload, Long id) {
        Users savedUsers = findById(id);

        payload.setId(id);
        if (Objects.nonNull(payload.getPassword()))
            payload.setPassword(encoder.encode(payload.getPassword()));
        copyNonNullProperties(payload, savedUsers);

        return userRepository.save(savedUsers);
    }

    @Override
    public Page<Users> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Cant find user"));
    }

    @Override
    public void delete(Long id) {
        Users users = findById(id);
        userRepository.delete(users);
    }

    @Override
    public Users login(Users payload) {
        Users savedUser = userRepository.findByEmailIgnoreCase(payload.getEmail())
                .orElseThrow(()-> new RuntimeException("user doesnt exists"));

        if (!encoder.matches(payload.getPassword(), savedUser.getPassword())){
            throw new RuntimeException("incorrect email/password");
        }

        payload.setToken(buildToken(savedUser));
        payload.setType(savedUser.getType());
        payload.setId(savedUser.getId());
        payload.setUsername(savedUser.getUsername());

        return payload;
    }

    private String buildToken(Users users){
        Map<String,Object> claims = Map.of("email", users.getEmail());
        return jwtService.generateToken(claims, users);
    }
}
