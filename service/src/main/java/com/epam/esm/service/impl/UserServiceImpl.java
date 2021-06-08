package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.UserAlreadyExistsException;
import com.epam.esm.model.User;
import com.epam.esm.model.UserRole;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.repository.specification.CommonSpecifications.*;
import static com.epam.esm.repository.specification.UserSpecifications.byLogin;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class UserServiceImpl implements UserService {
    private final MainRepository<User> userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        Specification<User> specification = typed(byId(id), User.class).and(notRemoved());
        return userRepository
                .queryFirst(specification)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository
                .queryFirst(byLogin(login))
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.query(all(), pageable);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByLogin(username);
    }

    @Override
    public User create(User user) {
        String login = user.getLogin();
        if (userRepository.exists(byLogin(login))) {
            throw new UserAlreadyExistsException(String.format("User with login: [%s] already exists", login));
        }

        user.setEnabled(true);
        user.setRole(UserRole.USER);

        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.add(user);
    }

    @Override
    public boolean userExists(String login) {
        return userRepository.exists(byLogin(login));
    }
}
