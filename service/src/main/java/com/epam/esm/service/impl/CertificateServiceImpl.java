package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.CertificateSpecifications;
import com.epam.esm.service.CertificateQueryObject;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.repository.specification.CertificateSpecifications.byDescription;
import static com.epam.esm.repository.specification.CommonSpecifications.*;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final MainRepository<Tag> tagRepository;
    private final MainRepository<Certificate> repository;

    @Override
    public Certificate findById(long id) {
        Specification<Certificate> specification = typed(byId(id), Certificate.class).and(notRemoved());
        Optional<Certificate> optionalCertificate = repository.queryFirst(specification);
        return optionalCertificate.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Certificate update(Certificate certificate) {
        Optional<Certificate> optional = repository.queryFirst(byId(certificate.getId()));
        Certificate oldCertificate = optional.orElseThrow(EntityNotFoundException::new);

        Certificate certificateToUpdate = merge(oldCertificate, certificate);
        List<Tag> ensuredTags = ensureTagsInRepo(certificateToUpdate.getTags());

        certificateToUpdate = certificateToUpdate.toBuilder()
                .lastUpdateDate(LocalDate.now())
                .tags(ensuredTags)
                .build();

        return repository.update(certificateToUpdate);
    }

    private Certificate merge(Certificate to, Certificate from) {
        if (StringUtils.isNotBlank(from.getName())) {
            to.setName(from.getName());
        }
        if (StringUtils.isNotBlank(from.getDescription())) {
            to.setDescription(from.getDescription());
        }
        if (from.getDuration() != null) {
            to.setDuration(from.getDuration());
        }
        if (from.getPrice() != null) {
            to.setPrice(from.getPrice());
        }
        if (from.getTags() != null) {
            to.setTags(from.getTags());
        }

        return to;
    }

    @Override
    public Certificate add(Certificate certificate) {
        List<Tag> tags = CollectionUtils.isNotEmpty(certificate.getTags())
                ? ensureTagsInRepo(certificate.getTags())
                : Collections.emptyList();

        certificate = certificate.toBuilder()
                .createDate(LocalDate.now())
                .lastUpdateDate(LocalDate.now())
                .tags(tags)
                .build();

        return repository.add(certificate);
    }

    private List<Tag> ensureTagsInRepo(List<Tag> tags) {
        Set<String> tagNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
        List<Tag> tagsInRepo = tagRepository.queryList(byNameIn(tagNames), Pageable.unpaged());
        tagsInRepo.forEach(tagInRepo -> tagNames.remove(tagInRepo.getName()));
        List<Tag> ensuredTags = new ArrayList<>(tagsInRepo);
        tags.stream().distinct()
                .filter(tag -> tagNames.contains(tag.getName()))
                .map(tagRepository::add)
                .forEach(ensuredTags::add);

        return ensuredTags;
    }

    @Override
    public Certificate remove(long id) {
        return repository.remove(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Certificate> findCertificatesByQueryObject(CertificateQueryObject queryObject,
                                                           Pageable pageable, boolean eager) {

        Specification<Certificate> specification = notRemoved();
        if (StringUtils.isNotBlank(queryObject.getName())) {
            specification = specification.and(byName(queryObject.getName()));
        }
        if (StringUtils.isNotBlank(queryObject.getDescription())) {
            specification = specification.and(byDescription(queryObject.getDescription()));
        }
        if (CollectionUtils.isNotEmpty(queryObject.getTagNames())) {
            List<Specification<Certificate>> tagNameSpecifications = queryObject.getTagNames()
                    .stream().distinct()
                    .map(CertificateSpecifications::byTagName)
                    .collect(Collectors.toList());
            for (Specification<Certificate> tagSpecification : tagNameSpecifications) {
                specification = specification.and(tagSpecification);
            }
        }

        return repository.query(specification, pageable, eager);
    }
}
