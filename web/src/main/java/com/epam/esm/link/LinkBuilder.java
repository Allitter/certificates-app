package com.epam.esm.link;

import org.springframework.hateoas.RepresentationModel;

public interface LinkBuilder<T extends RepresentationModel<? extends T>> {

    T buildLinks(T t);

    T buildLinksPaged(T t);
}
