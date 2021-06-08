package com.epam.esm.repository.specification;

import com.epam.esm.model.Model;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public final class CommonSpecifications {

    private CommonSpecifications() {
    }

    public static <T extends Model> Specification<T> all() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static <T extends Model> Specification<T> byId(long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    public static <T extends Model> Specification<T> notRemoved() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("removed"), false);
    }

    public static <T extends Model> Specification<T> typed(Specification<T> specification, Class<T> tClass) {
        return specification;
    }

    public static <T extends Model> Specification<T> byName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static <T extends Model> Specification<T> byNameIn(Collection<String> names) {
        return (root, query, criteriaBuilder) -> root.get("name").in(names);
    }
}
