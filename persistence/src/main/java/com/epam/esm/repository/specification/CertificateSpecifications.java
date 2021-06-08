package com.epam.esm.repository.specification;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public final class CertificateSpecifications {

    private CertificateSpecifications() {
    }

    public static Specification<Certificate> byDescription(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Certificate> byTagName(String tagName) {
        return (root, query, criteriaBuilder) -> {
            Join<Tag, Certificate> certificateTagJoin = root.join("tags");
            return criteriaBuilder.like(certificateTagJoin.get("name"), "%" + tagName + "%");
        };
    }
}
