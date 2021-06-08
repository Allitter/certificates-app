package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.model.Purchase;
import com.epam.esm.model.User;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;
import com.epam.esm.validation.ValidationGroup.Create;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PurchaseService purchaseService;
    private final LinkBuilder<UserDto> userLinkBuilder;
    private final LinkBuilder<PurchaseDto> purchaseLinkBuilder;
    private final PagedResourcesAssembler<User> pagedUserResourcesAssembler;
    private final PagedResourcesAssembler<Purchase> pagePurchasedResourcesAssembler;
    private final RepresentationModelAssembler<Purchase, PurchaseDto> purchaseModelAssembler;
    private final RepresentationModelAssemblerSupport<User, UserDto> userAssembler;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedModel<UserDto>> findAll(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        PagedModel<UserDto> dtos = pagedUserResourcesAssembler.toModel(users, userAssembler);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || @userSecurity.hasSameId(authentication, #id)")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserDto dto = EntityConverter.map(user);
        dto = userLinkBuilder.buildLinks(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}/purchases")
    @PreAuthorize("hasRole('ADMIN') || @userSecurity.hasSameId(authentication, #id)")
    public ResponseEntity<PagedModel<PurchaseDto>> findUserPurchases(@PathVariable Long id, Pageable pageable) {
        Page<Purchase> orders = purchaseService.findByUserId(id, pageable, false);
        PagedModel<PurchaseDto> dtos = pagePurchasedResourcesAssembler.toModel(orders, purchaseModelAssembler);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/purchases")
    @PreAuthorize("@userSecurity.hasSameId(authentication, #id)")
    public ResponseEntity<PurchaseDto> addPurchase(@PathVariable Long id,
                                                   @Validated(Create.class) @RequestBody PurchaseDto purchaseDto) {
        Purchase purchase = EntityConverter.map(purchaseDto);
        purchase = purchaseService.add(id, purchase);
        PurchaseDto dto = EntityConverter.map(purchase);
        dto = purchaseLinkBuilder.buildLinks(dto);
        return ResponseEntity.ok(dto);
    }
}
