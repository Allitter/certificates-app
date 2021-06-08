package com.epam.esm.dto;

import com.epam.esm.validation.ResourceBundleMessage;
import com.epam.esm.validation.ValidationGroup.Create;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "certificates")
public class PurchasesCertificateDto extends RepresentationModel<PurchasesCertificateDto> {
    @NotNull(message = ResourceBundleMessage.CERTIFICATE_PURCHASE_ID_EMPTY, groups = {Create.class})
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    @Builder.Default
    private List<TagDto> tags = new ArrayList<>();
    @NotNull(message = ResourceBundleMessage.CERTIFICATE_PURCHASE_COUNT_EMPTY)
    @Min(value = 1, message = ResourceBundleMessage.CERTIFICATE_PURCHASE_COUNT_FORMAT)
    private Integer count;

}
