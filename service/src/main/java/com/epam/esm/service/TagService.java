package com.epam.esm.service;

import com.epam.esm.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The Tag service interface.
 */
public interface TagService {

    /**
     * Find by id tag dto.
     *
     * @param id the id to search
     * @return {@link Tag}
     */
    Tag findById(long id);

    /**
     * Finds all tags
     *
     * @return the list of all tags
     */
    Page<Tag> findAll(Pageable pageable);

    /**
     * Adds tag to repository.
     *
     * @param tag the tag to be added
     * @return the added tag
     */
    Tag add(Tag tag);

    Tag findMostFrequentTagOfUserWithHighestCostOfAllOrders();

    /**
     * Remove boolean.
     *
     * @param id the id
     * @return true if removed, false otherwise
     */
    Tag remove(long id);

}
