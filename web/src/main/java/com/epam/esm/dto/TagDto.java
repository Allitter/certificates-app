package com.epam.esm.dto;

import com.epam.esm.validation.ResourceBundleMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = true)
@Relation(collectionRelation = "tags")
public class TagDto extends RepresentationModel<TagDto> {
    private Long id;
    @NotBlank(message = ResourceBundleMessage.TAG_NAME_EMPTY)
    @Size(min = 2, max = 64, message = ResourceBundleMessage.TAG_NAME_FORMAT)
    private String name;
}
