package com.epam.esm.repository;

import com.epam.esm.exception.SortArgumentException;
import com.epam.esm.model.Model;
import com.epam.esm.repository.query.NativeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.mapping.PropertyReferenceException;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@RequiredArgsConstructor
public abstract class AbstractRepository<T extends Model> implements MainRepository<T> {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    protected final EntityManager entityManager;

    protected abstract Class<T> getEntityType();

    protected abstract void fetchConnectedEntities(Root<T> root);

    @Override
    public T add(T model) {
        model.setId(null);
        entityManager.persist(model);
        return model;
    }

    @Override
    public Collection<T> addAll(Collection<T> models) {
        models.forEach(model -> model.setId(null));
        models.forEach(entityManager::persist);
        entityManager.flush();
        return new ArrayList<>(models);
    }

    @Override
    public T update(T model) {
        return entityManager.merge(model);
    }

    @Override
    public Optional<T> remove(long id) {
        T model = entityManager.find(getEntityType(), id);
        if (model != null) {
            entityManager.remove(model);
        }
        return Optional.ofNullable(model);
    }

    @Override
    public Optional<T> queryFirst(Specification<T> specification, boolean eager) {
        List<T> list = queryList(specification, Pageable.unpaged(), eager);
        return list.stream().findFirst();
    }

    @Override
    public Optional<T> queryFirst(NativeQuery nativeQuery) {
        List<T> list = queryList(nativeQuery, Pageable.unpaged());
        return list.stream().findFirst();
    }

    @Override
    public Page<T> query(Specification<T> specification, Pageable pageable, boolean eager) {
        List<T> items = queryList(specification, pageable, eager);
        long count = count(specification);
        return new PageImpl<>(items, pageable, count);
    }

    @Override
    public Page<T> query(NativeQuery nativeQuery, Pageable pageable) {
        List<T> items = queryList(nativeQuery, pageable);
        return new PageImpl<>(items, pageable, count(nativeQuery));
    }

    @Override
    public List<T> queryList(NativeQuery nativeQuery, Pageable pageable) {
        Query query = entityManager.createNativeQuery(nativeQuery.getQuery(), getEntityType());
        Map<String, Object> params = nativeQuery.getParams();
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        setResultPageIfPaged(pageable, query);

        return query.getResultList();
    }

    @Override
    public List<T> queryList(Specification<T> specification, Pageable pageable, boolean eager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityType());

        return eager
                ? queryEager(specification, pageable, criteriaBuilder, criteriaQuery)
                : queryLazy(specification, pageable, criteriaBuilder, criteriaQuery);
    }

    @Override
    public boolean exists(Specification<T> specification) {
        return count(specification) > 0;
    }

    private List<T> queryEager(Specification<T> specification, Pageable pageable,
                               CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery) {

        CriteriaQuery<Long> idQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = idQuery.from(getEntityType());
        Predicate predicate = specification.toPredicate(root, idQuery, criteriaBuilder);

        idQuery.select(root.get("id")).where(predicate);
        trySort(pageable, criteriaBuilder, idQuery, root);
        TypedQuery<Long> query = entityManager.createQuery(idQuery);
        setResultPageIfPaged(pageable, query);
        List<Long> ids = query.getResultList();

        root = criteriaQuery.from(getEntityType());

        fetchConnectedEntities(root);
        criteriaQuery.select(root).where(root.get("id").in(ids)).distinct(true);
        trySort(pageable, criteriaBuilder, criteriaQuery, root);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private List<T> queryLazy(Specification<T> specification, Pageable pageable,
                              CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery) {

        Root<T> root = criteriaQuery.from(getEntityType());
        Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
        criteriaQuery.select(root).where(predicate);

        trySort(pageable, criteriaBuilder, criteriaQuery, root);
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        setResultPageIfPaged(pageable, query);

        return query.getResultList();
    }

    private void trySort(Pageable pageable, CriteriaBuilder criteriaBuilder,
                         CriteriaQuery<?> criteriaQuery, Root<?> root) {
        try {
            if (pageable.getSort().isUnsorted()) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
            } else {
                criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), root, criteriaBuilder));
            }
        } catch (PropertyReferenceException e) {
            throw new SortArgumentException(e.getPropertyName());
        }
    }

    private void setResultPageIfPaged(Pageable pageable, Query query) {
        if (pageable.isPaged()) {
            int offset = (int) pageable.getOffset();
            query.setFirstResult(offset)
                    .setMaxResults(pageable.getPageSize());
        }
    }

    private long count(Specification<T> specification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityType());
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = countQuery.from(getEntityType());

        countQuery.select(criteriaBuilder.count(root));
        countQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder)).distinct(true);

        return entityManager
                .createQuery(countQuery)
                .getSingleResult();
    }

    private long count(NativeQuery nativeQuery) {
        Query query = entityManager.createNativeQuery(nativeQuery.getQuery(), Long.class);
        Map<String, Object> params = nativeQuery.getParams();

        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));

        return (long) query.getSingleResult();
    }
}
