package com.epam.esm.service;

import com.epam.esm.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseService {

    Purchase findById(Long id);

    Page<Purchase> findByUserId(Long idUser, Pageable pageable, boolean eager);

    Purchase add(Long idUser, Purchase purchase);

}
