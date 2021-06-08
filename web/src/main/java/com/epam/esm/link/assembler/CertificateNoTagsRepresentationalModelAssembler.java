package com.epam.esm.link.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.link.impl.CertificateLinkBuilder;
import com.epam.esm.model.Certificate;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateNoTagsRepresentationalModelAssembler extends RepresentationModelAssemblerSupport<Certificate, CertificateDto> {
    private final CertificateLinkBuilder linkBuilder;

    public CertificateNoTagsRepresentationalModelAssembler(CertificateLinkBuilder linkBuilder) {
        super(CertificateController.class, CertificateDto.class);
        this.linkBuilder = linkBuilder;
    }

    @Override
    public CertificateDto toModel(@NonNull Certificate certificate) {
        CertificateDto dto = EntityConverter.mapCertificateNoTags(certificate);
        dto.add(linkTo(methodOn(CertificateController.class).findCertificateTags(dto.getId())).withRel("tags"));
        return linkBuilder.buildLinksPaged(dto);
    }
}
