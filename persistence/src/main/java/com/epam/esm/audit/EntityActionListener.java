package com.epam.esm.audit;

import com.epam.esm.util.BeanUtil;

import javax.persistence.*;

public class EntityActionListener {

    @PostUpdate
    public void handlePostUpdate(Object object) {
        logAction(Audit.updateFor(object));
    }

    @PreRemove
    public void handlePreRemove(Object object) {
        logAction(Audit.removeFor(object));
    }

    public void logAction(Audit audit) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(audit);
    }
}
