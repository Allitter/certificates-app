package com.epam.esm.link.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.link.impl.TagLinkBuilder;
import com.epam.esm.model.Tag;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TagRepresentationalModelAssembler extends RepresentationModelAssemblerSupport<Tag, TagDto> {
    private final TagLinkBuilder linkBuilder;

    public TagRepresentationalModelAssembler(TagLinkBuilder linkBuilder) {
        super(TagController.class, TagDto.class);
        this.linkBuilder = linkBuilder;
    }

    @Override
    public TagDto toModel(@NonNull Tag tag) {
        TagDto dto = EntityConverter.map(tag);
        return linkBuilder.buildLinksPaged(dto);
    }
}
