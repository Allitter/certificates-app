package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Purchase;
import com.epam.esm.repository.AbstractRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Optional;

import static com.epam.esm.repository.specification.CommonSpecifications.*;

@Repository
public class PurchaseRepository extends AbstractRepository<Purchase> {

    protected PurchaseRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Purchase> getEntityType() {
        return Purchase.class;
    }

    @Override
    protected void fetchConnectedEntities(Root<Purchase> root) {
        root.fetch("purchaseCertificates", JoinType.LEFT);
    }

    @Override
    public Optional<Purchase> remove(long id) {
        Specification<Purchase> specification = typed(byId(id), Purchase.class).and(notRemoved());
        Optional<Purchase> purchaseOptional = queryFirst(specification);

        purchaseOptional.ifPresent(purchase -> purchase.setRemoved(true));
        return purchaseOptional;
    }
}
