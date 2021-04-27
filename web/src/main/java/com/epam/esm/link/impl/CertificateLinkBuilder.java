package com.epam.esm.link.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.link.LinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateLinkBuilder implements LinkBuilder<CertificateDto> {
    @Override
    public CertificateDto buildLinks(CertificateDto dto) {
        return dto.add(selfLink(dto))
                .add(linkToAll());
    }

    @Override
    public CertificateDto buildLinksPaged(CertificateDto dto) {
        return dto.add(selfLink(dto));
    }

    private Link selfLink(CertificateDto dto) {
        return linkTo(methodOn(CertificateController.class)
                .findById(dto.getId())).withSelfRel();
    }

    private Link linkToAll() {
        return linkTo(methodOn(CertificateController.class).findByQuery(null, null, true)).withRel("all");
    }
}
