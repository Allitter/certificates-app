package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.query.MostFrequentTagOfUserWithHighestCostOfAllOrdersQuery;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.epam.esm.repository.specification.CommonSpecifications.*;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class TagServiceImpl implements TagService {
    private final MainRepository<Tag> tagRepository;

    @Override
    public Tag findById(long id) {
        Optional<Tag> optionalTag = tagRepository.queryFirst(byId(id));
        return optionalTag.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return tagRepository.query(all(), pageable);
    }

    @Override
    public Tag add(Tag tag) {
        if (tagRepository.exists(byName(tag.getName()))) {
            throw new EntityAlreadyExistsException();
        }

        return tagRepository.add(tag);
    }

    @Override
    public Tag findMostFrequentTagOfUserWithHighestCostOfAllOrders() {
        return tagRepository
                .queryFirst(new MostFrequentTagOfUserWithHighestCostOfAllOrdersQuery())
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Tag remove(long id) {
        return tagRepository.remove(id).orElseThrow(EntityNotFoundException::new);
    }
}
