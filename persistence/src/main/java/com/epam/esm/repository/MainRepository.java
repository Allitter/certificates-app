package com.epam.esm.repository;

import com.epam.esm.model.Model;
import com.epam.esm.repository.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The interface Main repository.
 *
 * @param <T> the type of stored entities
 */
public interface MainRepository<T extends Model> {

    /**
     * Add.
     *
     * @param t the entity to be added
     * @return the added entity
     */
    T add(T t);

    Collection<T> addAll(Collection<T> t);

    /**
     * Update.
     *
     * @param t the entity to be updated
     * @return the updated entity
     */
    T update(T t);

    /**
     * Remove.
     *
     * @param id the id of entity to be removed
     * @return true if removed, false otherwise
     */
    Optional<T> remove(long id);

    /**
     * Query.
     *
     * @param specification the specification of queried entities {@link Specification}
     * @return the list of entities matching the specification
     */
    Page<T> query(Specification<T> specification, Pageable pageable, boolean eager);

    Page<T> query(NativeQuery nativeQuery, Pageable pageable);

    List<T> queryList(Specification<T> specification, Pageable pageable, boolean eager);

    List<T> queryList(NativeQuery nativeQuery, Pageable pageable);

    /**
     * Query first. The same as query() method but returns only the first entity if such exists
     *
     * @param specification the specification of queried entities {@link Specification}
     * @return first of entities matching the specification or empty optional
     */
    Optional<T> queryFirst(Specification<T> specification, boolean eager);

    Optional<T> queryFirst(NativeQuery nativeQuery);

    boolean exists(Specification<T> specification);

    default Page<T> query(Specification<T> specification, Pageable pageable) {
        return query(specification, pageable, true);
    }

    default List<T> queryList(Specification<T> specification, Pageable pageable) {
        return queryList(specification, pageable, true);
    }

    default Optional<T> queryFirst(Specification<T> specification) {
        return queryFirst(specification, true);
    }
}
