package com.epam.esm.service;

import com.epam.esm.model.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Certificate service interface.
 */
public interface CertificateService {

    /**
     * Finds certificate by id.
     *
     * @param id the id
     * @return {@link Certificate}
     */
    Certificate findById(long id);

    /**
     * Update certificate dto.
     *
     * @param certificate certificate to update
     * @return the updated certificate
     */
    Certificate update(Certificate certificate);

    /**
     * Add certificate dto.
     *
     * @param certificate certificate to add
     * @return the added certificate
     */
    Certificate add(Certificate certificate);

    /**
     * Remove boolean.
     *
     * @param id of certificate to remove
     * @return true if removed, false otherwise
     */
    Certificate remove(long id);

    /**
     * Find certificates by query object list.
     *
     * @param queryObject the query object
     * @return queried certificates
     */
    Page<Certificate> findCertificatesByQueryObject(CertificateQueryObject queryObject, Pageable pageable, boolean eager);
}
