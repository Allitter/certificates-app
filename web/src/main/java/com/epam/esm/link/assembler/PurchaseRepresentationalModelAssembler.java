package com.epam.esm.link.assembler;

import com.epam.esm.controller.PurchaseController;
import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.PurchasesDto;
import com.epam.esm.link.impl.PurchaseLinkBuilder;
import com.epam.esm.model.Purchase;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PurchaseRepresentationalModelAssembler extends RepresentationModelAssemblerSupport<Purchase, PurchasesDto> {
    private final PurchaseLinkBuilder linkBuilder;

    public PurchaseRepresentationalModelAssembler(PurchaseLinkBuilder linkBuilder) {
        super(PurchaseController.class, PurchasesDto.class);
        this.linkBuilder = linkBuilder;
    }

    @Override
    public PurchasesDto toModel(@NonNull Purchase purchase) {
        PurchasesDto dto = EntityConverter.map(purchase);
        dto.setCertificates(new ArrayList<>());
        return linkBuilder.buildLinksPaged(dto);
    }
}