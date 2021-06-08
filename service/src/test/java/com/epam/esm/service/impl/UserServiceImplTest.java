package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.User;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.impl.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UserServiceImplTest {
    private final MainRepository<User> userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder);

    @Test
    void testFindByIdShouldReturnUserWithMatchingIdIfExists() {
        User expected = new User();
        expected.setId(1L);
        expected.setLogin("login");
        expected.setPassword("pass");
        Mockito.when(userRepository.queryFirst(any(Specification.class)))
                .thenReturn(Optional.of(expected));

        User actual = userService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testFindByIdShouldThrowExceptionIfNoUserFound() {
        Mockito.when(userRepository.queryFirst(any(Specification.class)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindAllShouldReturnAllUsers() {
        List<User> expected = List.of(new User(), new User());
        Mockito.when(userRepository.query(any(Specification.class), eq(Pageable.unpaged())))
                .thenReturn(new PageImpl<>(expected));

        Page<User> actual = userService.findAll(Pageable.unpaged());

        assertEquals(expected, actual.getContent());
    }
}
