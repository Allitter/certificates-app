package com.epam.esm.repository.impl;

import com.epam.esm.model.Purchase;
import com.epam.esm.model.User;
import com.epam.esm.repository.AbstractRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Root;
import java.util.Optional;

import static com.epam.esm.repository.specification.CommonSpecifications.*;

@Repository
public class UserRepository extends AbstractRepository<User> {

    protected UserRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<User> getEntityType() {
        return User.class;
    }

    @Override
    protected void fetchConnectedEntities(Root<User> root) { // yet nothing to fetch
    }

    @Override
    public Optional<User> remove(long id) {
        Specification<User> specification = typed(byId(id), User.class).and(notRemoved());
        Optional<User> userOptional = queryFirst(specification);
        userOptional.ifPresent(user -> user.setRemoved(true));
        return userOptional;
    }
}
