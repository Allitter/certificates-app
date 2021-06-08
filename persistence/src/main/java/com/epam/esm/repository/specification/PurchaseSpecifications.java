package com.epam.esm.repository.specification;

import com.epam.esm.model.Purchase;
import org.springframework.data.jpa.domain.Specification;

public final class PurchaseSpecifications {

    private PurchaseSpecifications() {
    }

    public static Specification<Purchase> byUserId(long idUser) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), idUser);
    }
}
