package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Purchase;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.impl.PurchaseRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

class PurchaseServiceImplTest {
    private final MainRepository<Purchase> purchaseRepository = Mockito.mock(PurchaseRepository.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final CertificateService certificateService = Mockito.mock(CertificateService.class);
    private final PurchaseServiceImpl purchaseService = new PurchaseServiceImpl(userService, certificateService, purchaseRepository);

    @Test
    void testFindByIdShouldReturnPurchaseWithMatchingIdIfExists() {
        Purchase expected = new Purchase();
        expected.setId(1L);
        expected.setCost(100);
        Mockito.when(purchaseRepository.queryFirst(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expected));

        Purchase actual = purchaseService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testFindByIdShouldThrowExceptionIfNoPurchaseFound() {
        Mockito.when(purchaseRepository.queryFirst(Mockito.any(Specification.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> purchaseService.findById(1L));
    }

    @Test
    void testFindByUserIdShouldReturnPurchasesOfUserWithMatchingId() {
        List<Purchase> expected = List.of(new Purchase(), new Purchase());
        Mockito.when(purchaseRepository.query(Mockito.any(Specification.class), eq(Pageable.unpaged()), eq(false)))
                .thenReturn(new PageImpl<>(expected));

        Page<Purchase> actual = purchaseService.findByUserId(1L, Pageable.unpaged(), false);

        assertEquals(expected, actual.getContent());
    }

    @Test
    void testAddShouldAddPurchaseToRepository() {
        Purchase purchase = new Purchase();
        purchase.setPurchaseCertificates(new ArrayList<>());

        purchaseService.add(1L, purchase);

        Mockito.verify(purchaseRepository, Mockito.times(1)).add(purchase);
    }
}
