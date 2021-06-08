package com.epam.esm.link.assembler;

import com.epam.esm.controller.PurchaseController;
import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.link.impl.PurchaseLinkBuilder;
import com.epam.esm.model.Purchase;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseRepresentationalModelAssembler extends RepresentationModelAssemblerSupport<Purchase, PurchaseDto> {
    private final PurchaseLinkBuilder linkBuilder;

    public PurchaseRepresentationalModelAssembler(PurchaseLinkBuilder linkBuilder) {
        super(PurchaseController.class, PurchaseDto.class);
        this.linkBuilder = linkBuilder;
    }

    @Override
    public PurchaseDto toModel(@NonNull Purchase purchase) {
        PurchaseDto dto = EntityConverter.mapPurchaseNoCertificates(purchase);
        return linkBuilder.buildLinksPaged(dto);
    }
}
