package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.service.CertificateQueryObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;

class CertificateServiceImplTest {
    private static final String ANY_TEXT = "text";
    private static final long ID = 1;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final int PRICE = 100;
    private static final int DURATION = 100;
    private static final LocalDate CREATE_DATE = LocalDate.now();
    private static final LocalDate LAST_UPDATE_DATE = LocalDate.now();
    private static final Tag FIRST_TAG = new Tag(1L, "tag");
    private static final Tag SECOND_TAG = new Tag(2L, "another tag");
    private static final Set<Tag> TAGS = Set.of(FIRST_TAG, SECOND_TAG);
    private static final Certificate CERTIFICATE = Certificate.builder()
            .id(ID)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .duration(DURATION)
            .createDate(CREATE_DATE)
            .lastUpdateDate(LAST_UPDATE_DATE)
            .tags(new ArrayList<>(TAGS))
            .build();

    private static final Certificate SECOND_CERTIFICATE = Certificate.builder()
            .id(2L)
            .name("second name")
            .description("second description")
            .createDate(LocalDate.now().minusDays(1))
            .tags(List.of(FIRST_TAG))
            .build();

    private final MainRepository<Certificate> certificateRepository = Mockito.mock(MainRepository.class);
    private final MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
    private final CertificateServiceImpl service = new CertificateServiceImpl(tagRepository, certificateRepository);

    @Test
    void testFindByIdShouldReturnCertificateWithQueriedIdIfSuchExist() {
        Mockito.when(certificateRepository.queryFirst(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(CERTIFICATE));

        Certificate actual = service.findById(ID);

        assertEquals(CERTIFICATE, actual);
    }

    @Test
    void testAddShouldAddCertificateToRepository() {
        Mockito.when(certificateRepository.add(CERTIFICATE)).thenReturn(CERTIFICATE);
        Mockito.when(tagRepository.queryFirst(Mockito.isA(Specification.class))).thenReturn(Optional.empty());
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(tagRepository).add(any());

        Certificate actual = service.add(CERTIFICATE);
        Assertions.
                assertEquals(CERTIFICATE, actual);
    }

    @Test
    void TestUpdateShouldUpdateCertificate() {
        int newPrice = 1000;
        Certificate certificate = CERTIFICATE.toBuilder()
                .lastUpdateDate(LocalDate.now())
                .price(newPrice)
                .build();

        Mockito.when(certificateRepository.update(certificate)).thenReturn(certificate);
        Mockito.when(certificateRepository.queryFirst(isA(Specification.class))).thenReturn(Optional.of(CERTIFICATE));
        Mockito.when(tagRepository.queryFirst(Mockito.isA(Specification.class))).thenReturn(Optional.empty());
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(tagRepository).add(any());

        Certificate actual = service.update(certificate);

        assertEquals(certificate, actual);
    }

    @Test
    void TestUpdateShouldThrowExceptionIfCertificateNotPresent() {
        Mockito.when(certificateRepository.queryFirst(isA(Specification.class)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(CERTIFICATE));
    }

    @Test
    void testRemoveShouldReturnRemovedEntityIfRemoved() {
        Mockito.when(certificateRepository.remove(ID)).thenReturn(Optional.of(CERTIFICATE));

        Certificate actual = service.remove(ID);

        assertEquals(CERTIFICATE, actual);
    }

    @Test
    void testRemoveShouldThrowExceptionIfCertificateNotExists() {
        Mockito.when(certificateRepository.remove(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.remove(ID));
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindCertificatesByNameIfSet() {
        Mockito.when(certificateRepository.query(Mockito.any(Specification.class), eq(Pageable.unpaged()), eq(false)))
                .thenReturn(new PageImpl<>(List.of(CERTIFICATE)));
        CertificateQueryObject queryObject = new CertificateQueryObject(NAME, null, null);
        List<Certificate> expected = List.of(CERTIFICATE);

        Page<Certificate> actual = service.findCertificatesByQueryObject(queryObject, Pageable.unpaged(), false);

        assertEquals(expected, actual.getContent());
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindCertificatesByDescriptionIfSet() {
        Mockito.when(certificateRepository.query(Mockito.any(Specification.class), eq(Pageable.unpaged()), eq(false)))
                .thenReturn(new PageImpl<>(List.of(CERTIFICATE)));
        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, DESCRIPTION);
        List<Certificate> expected = List.of(CERTIFICATE);

        Page<Certificate> actual = service.findCertificatesByQueryObject(queryObject, Pageable.unpaged(), false);

        assertEquals(expected, actual.getContent());
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindCertificatesByTagNameIfSet() {
        Mockito.when(certificateRepository.query(Mockito.any(Specification.class), eq(Pageable.unpaged()), eq(false)))
                .thenReturn(new PageImpl<>(List.of(CERTIFICATE)));
        CertificateQueryObject queryObject = new CertificateQueryObject(null, Collections.singletonList(FIRST_TAG.getName()), null);
        List<Certificate> expected = List.of(CERTIFICATE);

        Page<Certificate> actual = service.findCertificatesByQueryObject(queryObject, Pageable.unpaged(), false);

        assertEquals(expected, actual.getContent());
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindAllCertificatesIfNoParametersSet() {
        Mockito.when(certificateRepository.query(Mockito.any(Specification.class), eq(Pageable.unpaged()), eq(false)))
                .thenReturn(new PageImpl<>(List.of(CERTIFICATE)));
        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, null);
        List<Certificate> expected = List.of(CERTIFICATE);

        Page<Certificate> actual = service.findCertificatesByQueryObject(queryObject, Pageable.unpaged(), false);

        assertEquals(expected, actual.getContent());
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindCertificatesByMultipleParametersIfSet() {
        Mockito.when(certificateRepository.query(Mockito.any(Specification.class), eq(Pageable.unpaged()), eq(false)))
                .thenReturn(new PageImpl<>(List.of(CERTIFICATE)));

        CertificateQueryObject queryObject = new CertificateQueryObject(NAME, Collections.singletonList(FIRST_TAG.getName()), DESCRIPTION);
        List<Certificate> expected = List.of(CERTIFICATE);

        Page<Certificate> actual = service.findCertificatesByQueryObject(queryObject, Pageable.unpaged(), false);

        assertEquals(expected, actual.getContent());
    }

    @Test
    void testFindCertificatesByQueryObjectShouldSortCertificatesByByNameAscendingIfAscSet() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "name");

        Mockito.when(certificateRepository.query(Mockito.any(Specification.class), eq(pageable), eq(false)))
                .thenReturn(new PageImpl<>(List.of(CERTIFICATE, SECOND_CERTIFICATE)));

        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, null);
        List<Certificate> expected = List.of(CERTIFICATE, SECOND_CERTIFICATE);

        Page<Certificate> actual = service.findCertificatesByQueryObject(queryObject, pageable, false);

        assertEquals(expected, actual.getContent());
    }
}
