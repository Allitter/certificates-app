package com.epam.esm.dto;

import com.epam.esm.validation.ResourceBundleMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.epam.esm.validation.ValidationGroup.Create;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "certificates")
public class CertificateDto extends RepresentationModel<CertificateDto> {
    private Long id;
    @NotBlank(message = ResourceBundleMessage.CERTIFICATE_NAME_EMPTY, groups = {Create.class})
    @Size(min = 2, max = 64, message = ResourceBundleMessage.CERTIFICATE_NAME_FORMAT)
    private String name;
    @NotBlank(message = ResourceBundleMessage.CERTIFICATE_DESCRIPTION_EMPTY, groups = {Create.class})
    @Size(min = 2, max = 255, message = ResourceBundleMessage.CERTIFICATE_DESCRIPTION_FORMAT)
    private String description;
    @NotNull(message = ResourceBundleMessage.CERTIFICATE_PRICE_EMPTY, groups = {Create.class})
    @Min(value = 0, message = ResourceBundleMessage.CERTIFICATE_PRICE_FORMAT)
    @Max(value = 999999, message = ResourceBundleMessage.CERTIFICATE_PRICE_FORMAT)
    private Integer price;
    @NotNull(message = ResourceBundleMessage.CERTIFICATE_DURATION_EMPTY, groups = {Create.class})
    @Min(value = 1, message = ResourceBundleMessage.CERTIFICATE_DURATION_FORMAT)
    @Max(value = Integer.MAX_VALUE, message = ResourceBundleMessage.CERTIFICATE_DURATION_FORMAT)
    private Integer duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    @Valid
    @Builder.Default
    private Set<TagDto> tags = new LinkedHashSet<>();

}


