package com.epam.esm.service;

import com.epam.esm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findById(Long idUser);

    User findByLogin(String login);

    Page<User> findAll(Pageable pageable);

    User create(User user);

    boolean userExists(String login);
}
