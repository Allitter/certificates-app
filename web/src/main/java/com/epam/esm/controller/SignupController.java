package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.SignupRequest;
import com.epam.esm.dto.UserDto;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final UserService userService;

    @PostMapping("/signup")
    @PreAuthorize("true")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequest signupRequest) {
        User user = EntityConverter.map(signupRequest);
        user = userService.create(user);
        UserDto dto = EntityConverter.map(user);
        return ResponseEntity.ok(dto);
    }
}
