package com.epam.esm.repository.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Root;

@Repository
public class TagRepository extends AbstractRepository<Tag> {

    public TagRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Tag> getEntityType() {
        return Tag.class;
    }

    @Override
    protected void fetchConnectedEntities(Root<Tag> root) { // nothing to fetch
    }
}
