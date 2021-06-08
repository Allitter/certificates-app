package com.epam.esm.link.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.link.LinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinkBuilder implements LinkBuilder<UserDto> {

    @Override
    public UserDto buildLinks(UserDto dto) {
        return dto
                .add(selfLink(dto))
                .add(linkToAll());
    }

    @Override
    public UserDto buildLinksPaged(UserDto dto) {
        return dto.add(selfLink(dto));
    }

    private Link selfLink(UserDto dto) {
        return linkTo(methodOn(UserController.class)
                .findById(dto.getId()))
                .withSelfRel();
    }

    private Link linkToAll() {
        return linkTo(methodOn(UserController.class).findAll(null)).withRel("all");
    }
}
