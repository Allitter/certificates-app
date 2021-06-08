package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.link.assembler.CertificateNoTagsRepresentationalModelAssembler;
import com.epam.esm.link.assembler.CertificateRepresentationalModelAssembler;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.CertificateQueryObject;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validation.ValidationGroup.Create;
import com.epam.esm.validation.ValidationGroup.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The controller to provide CRUD operations on {@link Certificate}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    private final LinkBuilder<CertificateDto> certificateLinkBuilder;
    private final LinkBuilder<TagDto> tagLinkBuilder;
    private final PagedResourcesAssembler<Certificate> pagedResourcesAssembler;
    private final CertificateRepresentationalModelAssembler certificateAssembler;
    private final CertificateNoTagsRepresentationalModelAssembler certificateNoTagsAssembler;

    /**
     * Search for certificates by passed params.
     *
     * @param query Object containing all params
     * @return the list of queried certificates or all certificates if no params passed
     */
    @GetMapping()
    @PreAuthorize("true")
    public ResponseEntity<PagedModel<CertificateDto>> findByQuery(CertificateQueryObject query,
                                                                  Pageable pageable,
                                                                  @RequestParam(defaultValue = "true") boolean fetchTags) {
        Page<Certificate> certificates = certificateService.findCertificatesByQueryObject(query, pageable, fetchTags);
        var assembler = fetchTags ? certificateAssembler : certificateNoTagsAssembler;
        PagedModel<CertificateDto> dtos = pagedResourcesAssembler.toModel(certificates, assembler);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Find by id.
     *
     * @param id the id of certificate
     * @return the {@link CertificateDto} of queried certificate
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<CertificateDto> findById(@PathVariable long id) {
        Certificate certificate = certificateService.findById(id);
        CertificateDto dto = EntityConverter.map(certificate);
        dto = certificateLinkBuilder.buildLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity<CollectionModel<TagDto>> findCertificateTags(@PathVariable long id) {
        Certificate certificate = certificateService.findById(id);
        List<Tag> tags = certificate.getTags();
        List<TagDto> tagDtos = tags.stream()
                .map(EntityConverter::map)
                .map(tagLinkBuilder::buildLinks)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(tagDtos));
    }

    /**
     * Add certificate.
     *
     * @param dto The certificate to add
     * @return the {@link CertificateDto} of added certificate
     */
    @PostMapping()
    @PreAuthorize("hasAuthority('certificate:create')")
    public ResponseEntity<CertificateDto> add(@Validated(Create.class) @RequestBody CertificateDto dto) {
        Certificate certificate = EntityConverter.map(dto);
        Certificate result = certificateService.add(certificate);
        CertificateDto certificateDto = EntityConverter.map(result);
        certificateDto = certificateLinkBuilder.buildLinks(certificateDto);
        return ResponseEntity.ok(certificateDto);
    }

    /**
     * Update certificate.
     *
     * @param id  the id of certificate to update
     * @param dto the updated fields of certificate
     * @return the updated certificate {@link CertificateDto}
     */
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('certificate:update')")
    public ResponseEntity<CertificateDto> update(@PathVariable long id,
                                                 @Validated(Update.class) @RequestBody CertificateDto dto) {
        dto.setId(id);
        Certificate certificate = EntityConverter.map(dto);
        Certificate updated = certificateService.update(certificate);
        CertificateDto certificateDto = EntityConverter.map(updated);
        certificateDto = certificateLinkBuilder.buildLinks(certificateDto);
        return ResponseEntity.ok(certificateDto);
    }

    /**
     * Delete certificate.
     *
     * @param id the id of certificate
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('certificate:delete')")
    public ResponseEntity<CertificateDto> remove(@PathVariable long id) {
        Certificate certificate = certificateService.remove(id);
        CertificateDto dto = EntityConverter.map(certificate);
        return ResponseEntity.ok(dto);
    }
}
