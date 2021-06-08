package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.AbstractRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Optional;

import static com.epam.esm.repository.specification.CommonSpecifications.*;

@Repository
public class CertificateRepository extends AbstractRepository<Certificate> {

    public CertificateRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Certificate> getEntityType() {
        return Certificate.class;
    }

    @Override
    protected void fetchConnectedEntities(Root<Certificate> root) {
        root.fetch("tags", JoinType.LEFT);
    }


    @Override
    public Optional<Certificate> remove(long id) {
        Specification<Certificate> specification = typed(byId(id), Certificate.class).and(notRemoved());
        Optional<Certificate> certificateOptional = queryFirst(specification);
        certificateOptional.ifPresent(certificate -> certificate.setRemoved(true));
        return certificateOptional;
    }
}
