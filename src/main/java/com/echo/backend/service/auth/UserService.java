package com.echo.backend.service.auth;

import com.echo.backend.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Users save(Users payload);

    Users update(Users payload, Long id);

    Users updatePartial(Users payload, Long id);

    Page<Users> findAllUsers(Pageable pageable);

    Users findById(Long id);

    void delete(Long id);

    Users login(Users payload);
}
