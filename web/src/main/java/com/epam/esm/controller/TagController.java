package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.ValidationGroup.Create;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Tag CRD controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final LinkBuilder<TagDto> tagLinkBuilder;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;
    private final RepresentationModelAssemblerSupport<Tag, TagDto> tagAssembler;

    /**
     * Fids all tags.
     *
     * @return the list of all tags
     */
    @GetMapping()
    @PreAuthorize("hasAuthority('tag:read')")
    public ResponseEntity<PagedModel<TagDto>> findAll(Pageable pageable) {
        Page<Tag> tags = tagService.findAll(pageable);
        PagedModel<TagDto> dtos = pagedResourcesAssembler.toModel(tags, tagAssembler);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Find tag by id.
     *
     * @param id the id of tag
     * @return the tag with queried id {@link TagDto}
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('tag:read')")
    public ResponseEntity<TagDto> findById(@PathVariable long id) {
        Tag tag = tagService.findById(id);
        TagDto dto = EntityConverter.map(tag);
        dto = tagLinkBuilder.buildLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/top-user/popular-tag")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TagDto> findMostFrequentTagOfUserWithHighestCostOfAllOrders() {
        Tag tag = tagService.findMostFrequentTagOfUserWithHighestCostOfAllOrders();
        TagDto dto = EntityConverter.map(tag);
        dto = tagLinkBuilder.buildLinks(dto);
        return ResponseEntity.ok(dto);
    }

    /**
     * Add tag.
     *
     * @param dto the tag to add
     * @return the added tag {@link TagDto}
     */
    @PostMapping()
    @PreAuthorize("hasAuthority('tag:create')")
    public ResponseEntity<TagDto> add(@Validated(Create.class) @RequestBody TagDto dto) {
        Tag tag = EntityConverter.map(dto);
        Tag result = tagService.add(tag);
        dto = EntityConverter.map(result);
        dto = tagLinkBuilder.buildLinks(dto);
        return ResponseEntity.ok(dto);
    }

    /**
     * Delete tag.
     *
     * @param id the id of tag to remove
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('tag:delete')")
    public ResponseEntity<TagDto> remove(@PathVariable long id) {
        Tag tag = tagService.remove(id);
        TagDto dto = EntityConverter.map(tag);
        return ResponseEntity.ok(dto);
    }
}
