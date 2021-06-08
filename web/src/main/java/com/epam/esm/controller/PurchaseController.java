package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.dto.PurchasesCertificateDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.model.Purchase;
import com.epam.esm.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final LinkBuilder<PurchaseDto> orderLinkBuilder;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase:read')")
    public ResponseEntity<PurchaseDto> findById(@PathVariable Long id) {
        Purchase purchase = purchaseService.findById(id);
        PurchaseDto dto = EntityConverter.mapPurchaseNoCertificates(purchase);
        dto = orderLinkBuilder.buildLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{idPurchase}/certificates")
    @PreAuthorize("hasAuthority('purchase:read')")
    public ResponseEntity<CollectionModel<PurchasesCertificateDto>> findPurchaseCertificates(@PathVariable Long idPurchase) {
        Purchase purchase = purchaseService.findById(idPurchase);
        PurchaseDto dto = EntityConverter.map(purchase);
        dto = orderLinkBuilder.buildLinks(dto);

        return ResponseEntity.ok(CollectionModel.of(dto.getCertificates()));
    }

    @GetMapping("/{idPurchase}/certificates/{idCertificate}")
    @PreAuthorize("hasAuthority('purchase:read')")
    public ResponseEntity<PurchasesCertificateDto> findPurchaseCertificate(@PathVariable Long idPurchase, @PathVariable Long idCertificate) {
        Purchase purchase = purchaseService.findById(idPurchase);
        PurchaseDto dto = EntityConverter.map(purchase);
        dto = orderLinkBuilder.buildLinks(dto);

        PurchasesCertificateDto certificateDto
                = dto.getCertificates()
                .stream()
                .filter(certificate -> certificate.getId().equals(idCertificate))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);

        return ResponseEntity.ok(certificateDto);
    }
}
