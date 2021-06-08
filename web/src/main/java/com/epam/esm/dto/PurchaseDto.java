package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "purchases")
public class PurchaseDto extends RepresentationModel<PurchaseDto> {
    private Long id;
    private Long idUser;
    private Integer cost;
    private LocalDateTime createTime;
    @Valid
    @Builder.Default
    private List<PurchasesCertificateDto> certificates = new ArrayList<>();

}
