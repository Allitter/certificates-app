package com.epam.esm.link.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.link.LinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagLinkBuilder implements LinkBuilder<TagDto> {

    @Override
    public TagDto buildLinks(TagDto dto) {
        return dto
                .add(selfLink(dto))
                .add(linkToAll());
    }

    @Override
    public TagDto buildLinksPaged(TagDto dto) {
        return dto.add(selfLink(dto));
    }

    private Link selfLink(TagDto dto) {
        return linkTo(methodOn(TagController.class)
                .findById(dto.getId()))
                .withSelfRel();
    }

    private Link linkToAll() {
        return linkTo(methodOn(TagController.class).findAll(null)).withRel("all");
    }
}
