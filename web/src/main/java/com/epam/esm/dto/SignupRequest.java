package com.epam.esm.dto;

import com.epam.esm.validation.ResourceBundleMessage;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SignupRequest {
    @Size(min = 2, max = 255, message = ResourceBundleMessage.USER_LOGIN_FORMAT)
    private final String login;
    private final String password;
}
