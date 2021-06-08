package com.epam.esm.link.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.UserDto;
import com.epam.esm.link.impl.UserLinkBuilder;
import com.epam.esm.model.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserRepresentationalModelAssembler extends RepresentationModelAssemblerSupport<User, UserDto> {
    private final UserLinkBuilder linkBuilder;

    public UserRepresentationalModelAssembler(UserLinkBuilder linkBuilder) {
        super(UserController.class, UserDto.class);
        this.linkBuilder = linkBuilder;
    }

    @Override
    public UserDto toModel(@NonNull User user) {
        UserDto dto = EntityConverter.map(user);
        return linkBuilder.buildLinksPaged(dto);
    }
}
