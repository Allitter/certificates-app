package com.epam.esm.converter;

import com.epam.esm.dto.*;
import com.epam.esm.model.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class EntityConverter {

    private EntityConverter() {
    }

    public static CertificateDto map(Certificate certificate) {
        Set<TagDto> tagDtos = null;
        if (CollectionUtils.isNotEmpty(certificate.getTags())) {
            tagDtos = certificate.getTags().stream()
                    .map(EntityConverter::map)
                    .collect(Collectors.toSet());
        }

        return CertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .price(certificate.getPrice())
                .duration(certificate.getDuration())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .tags(tagDtos)
                .build();
    }

    public static CertificateDto mapCertificateNoTags(Certificate certificate) {
        return CertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .price(certificate.getPrice())
                .duration(certificate.getDuration())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .tags(Collections.emptySet())
                .build();
    }

    public static Certificate map(CertificateDto dto) {
        List<Tag> tags = null;
        if (CollectionUtils.isNotEmpty(dto.getTags())) {
            tags = dto.getTags().stream()
                    .map(EntityConverter::map)
                    .collect(Collectors.toList());
        }

        return Certificate.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .duration(dto.getDuration())
                .createDate(dto.getCreateDate())
                .lastUpdateDate(dto.getLastUpdateDate())
                .tags(tags)
                .build();
    }

    public static Tag map(TagDto dto) {
        return new Tag(dto.getId(), dto.getName());
    }

    public static User map(SignupRequest dto) {
        return User.builder()
                .login(dto.getLogin())
                .password(dto.getPassword())
                .build();
    }


    public static TagDto map(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

    public static PurchaseDto mapPurchaseNoCertificates(Purchase purchase) {
        return PurchaseDto.builder()
                .id(purchase.getId())
                .idUser(purchase.getUser().getId())
                .cost(purchase.getCost())
                .certificates(Collections.emptyList())
                .createTime(purchase.getCreateTime())
                .build();
    }

    public static PurchaseDto map(Purchase purchase) {
        List<PurchasesCertificateDto> certificates = purchase.getPurchaseCertificates()
                .stream()
                .map(EntityConverter::map)
                .collect(Collectors.toList());

        return PurchaseDto.builder()
                .id(purchase.getId())
                .idUser(purchase.getUser().getId())
                .cost(purchase.getCost())
                .certificates(certificates)
                .createTime(purchase.getCreateTime())
                .build();
    }

    public static Purchase map(PurchaseDto purchaseDto) {
        List<PurchaseCertificate> purchaseCertificates = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(purchaseDto.getCertificates())) {
            purchaseCertificates = purchaseDto.getCertificates()
                    .stream()
                    .map(EntityConverter::map)
                    .collect(Collectors.toList());
        }

        return Purchase.builder()
                .id(purchaseDto.getId())
                .createTime(purchaseDto.getCreateTime())
                .purchaseCertificates(purchaseCertificates)
                .build();
    }

    public static PurchasesCertificateDto map(PurchaseCertificate purchaseCertificate) {
        Certificate certificate = purchaseCertificate.getCertificate();
        List<TagDto> tagDtos = certificate.getTags().stream()
                .map(EntityConverter::map)
                .collect(Collectors.toList());

        return PurchasesCertificateDto.builder()
                .count(purchaseCertificate.getCount())
                .id(certificate.getId())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .price(certificate.getPrice())
                .tags(tagDtos)
                .build();
    }

    public static PurchaseCertificate map(PurchasesCertificateDto dto) {
        Certificate certificate = Certificate.builder()
                .id(dto.getId())
                .build();
        return PurchaseCertificate.builder()
                .certificate(certificate)
                .count(dto.getCount())
                .build();
    }

    public static UserDto map(User user) {
        return new UserDto(user.getId(), user.getLogin());
    }

    public static User map(UserDto userDto) {
        return new User(userDto.getId(), userDto.getLogin());
    }
}
