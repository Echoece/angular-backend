package com.echo.backend.service.auth;

import com.echo.backend.entity.auth.Users;
import com.echo.backend.exception.customException.ApiAuthorizationException;
import com.echo.backend.exception.customException.ApiNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Users save(Users payload);

    Users update(Users payload, Long id) throws ApiNotFoundException;

    Users updatePartial(Users payload, Long id) throws ApiNotFoundException;

    Page<Users> findAllUsers(Pageable pageable);

    Users findById(Long id) throws ApiNotFoundException;

    void delete(Long id) throws ApiNotFoundException;

    Users login(Users payload) throws ApiAuthorizationException;
}
