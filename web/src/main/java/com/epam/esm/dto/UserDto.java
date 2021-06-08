package com.epam.esm.dto;

import com.epam.esm.validation.ResourceBundleMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "users")
public class UserDto extends RepresentationModel<UserDto> implements Serializable {
    private Long id;
    @Size(min = 2, max = 255, message = ResourceBundleMessage.USER_LOGIN_FORMAT)
    private String login;
}
