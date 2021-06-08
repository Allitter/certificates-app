package com.epam.esm.repository.specification;

import com.epam.esm.model.User;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<User> byLogin(String login) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("login"), login);
    }
}
