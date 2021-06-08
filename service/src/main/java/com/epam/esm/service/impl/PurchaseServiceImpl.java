package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Purchase;
import com.epam.esm.model.PurchaseCertificate;
import com.epam.esm.model.User;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.repository.specification.CommonSpecifications.*;
import static com.epam.esm.repository.specification.PurchaseSpecifications.byUserId;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class PurchaseServiceImpl implements PurchaseService {
    private final UserService userService;
    private final CertificateService certificateService;
    private final MainRepository<Purchase> purchaseRepository;

    @Override
    public Purchase findById(Long id) {
        Specification<Purchase> specification = typed(byId(id), Purchase.class).and(notRemoved());
        return purchaseRepository
                .queryFirst(specification)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Purchase> findByUserId(Long idUser, Pageable pageable, boolean eager) {
        Specification<Purchase> specification = byUserId(idUser).and(notRemoved());
        return purchaseRepository.query(specification, pageable, eager);
    }

    @Override
    public Purchase add(Long idUser, Purchase purchase) {
        List<PurchaseCertificate> compressedPurchaseCertificates
                = compressPurchaseCertificates(purchase.getPurchaseCertificates());
        purchase.setPurchaseCertificates(compressedPurchaseCertificates);

        purchase.getPurchaseCertificates().forEach(purchaseCertificate -> {
            Long id = purchaseCertificate.getCertificate().getId();
            purchaseCertificate.setCertificate(certificateService.findById(id));
            purchaseCertificate.setPurchase(purchase);
        });

        int cost = purchase.getPurchaseCertificates()
                .stream()
                .mapToInt(orderCertificate -> {
                    int price = orderCertificate.getCertificate().getPrice();
                    int count = orderCertificate.getCount();
                    return price * count;
                })
                .sum();

        User user = userService.findById(idUser);
        purchase.setUser(user);
        purchase.setCost(cost);
        purchase.setCreateTime(LocalDateTime.now());
        return purchaseRepository.add(purchase);
    }

    private List<PurchaseCertificate> compressPurchaseCertificates(List<PurchaseCertificate> purchaseCertificates) {
        Map<Long, PurchaseCertificate> purchaseCertificateMap = new HashMap<>(); // key is certificate id
        purchaseCertificates.forEach(purchaseCertificate ->
                putToMapOrCombineCount(purchaseCertificateMap, purchaseCertificate));

        return new ArrayList<>(purchaseCertificateMap.values());
    }

    // Certificate id is used as purchaseCertificateMap key
    private void putToMapOrCombineCount(Map<Long, PurchaseCertificate> purchaseCertificateMap,
                                        PurchaseCertificate purchaseCertificate) {

        Long certificateId = purchaseCertificate.getCertificate().getId();
        PurchaseCertificate previousValue = purchaseCertificateMap.put(certificateId, purchaseCertificate);
        if (previousValue != null) {
            int newCount = purchaseCertificate.getCount() + previousValue.getCount();
            purchaseCertificate.setCount(newCount);
        }
    }
}
