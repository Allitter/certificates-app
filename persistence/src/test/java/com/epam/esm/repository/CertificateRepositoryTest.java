package com.epam.esm.repository;

import com.epam.esm.config.TestConfig;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.impl.CertificateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.specification.CertificateSpecifications.byDescription;
import static com.epam.esm.repository.specification.CertificateSpecifications.byTagName;
import static com.epam.esm.repository.specification.CommonSpecifications.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest(classes = TestConfig.class)
class CertificateRepositoryTest {
    private static final int FIRST_TAG_INDEX = 0;
    private static final int SECOND_TAG_INDEX = 1;
    private static final int THIRD_TAG_INDEX = 2;
    private static final int FOURTH_TAG_INDEX = 3;
    private static final int FIRST_ELEMENT = 0;
    private static final long NON_EXISTING_ID = -1;
    private static final long FIRST_ELEMENT_ID = 1;
    private static final int SECOND_ELEMENT = 1;
    private static final int THIRD_ELEMENT = 2;
    private static final int FOURTH_ELEMENT = 3;

    private static final List<Tag> TAGS = List.of(
            new Tag(1L, "first tag"),
            new Tag(2L, "second tag"),
            new Tag(3L, "third tag"),
            new Tag(4L, "fourth tag")
    );
    private static final List<Certificate> CERTIFICATES = List.of(
            Certificate.builder()
                    .id(FIRST_ELEMENT_ID)
                    .name("first certificate")
                    .description("detailed description for first certificate")
                    .price(200).duration(365)
                    .createDate(LocalDate.of(2021, 1, 21))
                    .lastUpdateDate(LocalDate.of(2021, 2, 21))
                    .tags(List.of(
                            TAGS.get(FIRST_TAG_INDEX),
                            TAGS.get(THIRD_TAG_INDEX),
                            TAGS.get(FOURTH_TAG_INDEX)))
                    .build(),

            Certificate.builder()
                    .id(2L).name("second certificate")
                    .description("detailed description for second certificate")
                    .price(150).duration(365)
                    .createDate(LocalDate.of(2021, 2, 21))
                    .lastUpdateDate(LocalDate.of(2021, 3, 21))
                    .tags(List.of(TAGS.get(SECOND_TAG_INDEX)))
                    .build(),

            Certificate.builder()
                    .id(3L).name("third certificate")
                    .description("detailed description for third certificate")
                    .price(80).duration(365)
                    .createDate(LocalDate.of(2021, 1, 21))
                    .lastUpdateDate(LocalDate.of(2021, 2, 21))
                    .tags(List.of(
                            TAGS.get(SECOND_TAG_INDEX),
                            TAGS.get(THIRD_TAG_INDEX)))
                    .build(),

            Certificate.builder()
                    .id(4L).name("fourth certificate")
                    .description("detailed description for fourth certificate")
                    .price(200).duration(730)
                    .createDate(LocalDate.of(2020, 12, 21))
                    .lastUpdateDate(LocalDate.of(2020, 12, 31))
                    .tags(List.of(
                            TAGS.get(FIRST_TAG_INDEX),
                            TAGS.get(THIRD_TAG_INDEX)))
                    .build()
    );


    @Qualifier("certificateRepository")
    @Autowired
    private CertificateRepository certificateRepository;

    public CertificateRepositoryTest() {
    }

    public static Object[][] queries() {
        return new Object[][]{
                {all(), CERTIFICATES},
                {byName("first"), List.of(CERTIFICATES.get(FIRST_ELEMENT))},
                {byDescription("fourth"), List.of(CERTIFICATES.get(FOURTH_ELEMENT))},
                {byTagName("second"), List.of(CERTIFICATES.get(SECOND_ELEMENT), CERTIFICATES.get(THIRD_ELEMENT))},
                {byId(FIRST_ELEMENT_ID), List.of(CERTIFICATES.get(FIRST_ELEMENT))},
        };
    }

    @Test
    @Rollback
    void testAddShouldAddCertificateToDataSourceIfCertificateNotYetCreated() {
        Certificate expected = Certificate.builder()
                .id(null).name("fifth certificate")
                .description("detailed description for fifth certificate")
                .price(200).duration(730)
                .createDate(LocalDate.of(2020, 12, 21))
                .lastUpdateDate(LocalDate.of(2020, 12, 31)).build();

        Certificate actual = certificateRepository.add(expected);

        assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void testUpdateShouldUpdateCertificateTIfCertificateIsAlreadyExist() {
        Certificate expected = CERTIFICATES.get(FIRST_ELEMENT);
        String newDescription = "new description";
        expected = expected.toBuilder()
                .description(newDescription)
                .build();
        Certificate actual = certificateRepository.update(expected);

        assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void testRemoveShouldReturnNonEmptyOptionalIfDeletedCertificateFromDataSource() {
        Optional<Certificate> certificateOptional = certificateRepository.remove(FIRST_ELEMENT_ID);
        assertTrue(certificateOptional.isPresent());
    }

    @Test
    @Rollback
    void testRemoveShouldReturnEmptyOptionalIfNotDeletedCertificateFromDataSource() {
        Optional<Certificate> certificateOptional = certificateRepository.remove(NON_EXISTING_ID);

        assertTrue(certificateOptional.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("queries")
    @Rollback
    void testQueryShouldReturnListOfCertificatesMatchingTheSpecification(Specification<Certificate> specification,
                                                                         List<Certificate> expected) {
        Page<Certificate> actual = certificateRepository.query(specification, Pageable.unpaged(), true);

        assertEquals(expected, actual.getContent());
    }

    @Test
    @Rollback
    void testQuerySingleShouldReturnFirstResultForSpecification() {
        Certificate expected = CERTIFICATES.get(FIRST_ELEMENT);
        Specification<Certificate> specification = byId(FIRST_ELEMENT_ID);

        Certificate actual = certificateRepository.queryFirst(specification).get();

        assertEquals(expected, actual);
    }
}

