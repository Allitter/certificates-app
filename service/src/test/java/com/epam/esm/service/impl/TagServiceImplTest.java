package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.impl.TagRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TagServiceImplTest {
    private static final long FIRST_TAG_ID = 1;
    private static final String FIRST_TAG_NAME = "first";
    private static final Tag FIRST_TAG = new Tag(FIRST_TAG_ID, FIRST_TAG_NAME);
    private static final long SECOND_TAG_ID = 2;
    private static final String SECOND_TAG_NAME = "second";
    private static final Tag SECOND_TAG = new Tag(SECOND_TAG_ID, SECOND_TAG_NAME);
    private static final long NON_EXISTING_ID = -1;

    private final MainRepository<Tag> repository = Mockito.mock(TagRepository.class);

    @Test
    void testFindByIdShouldReturnTagDtoWithPassedId() {
        Mockito.when(repository.queryFirst(Mockito.isA(Specification.class)))
                .thenReturn(Optional.of(FIRST_TAG));
        TagServiceImpl service = new TagServiceImpl(repository);

        Tag actual = service.findById(FIRST_TAG_ID);

        assertEquals(FIRST_TAG, actual);
    }

    @Test
    void testFindByIdShouldThrowExceptionIfEntityNotFound() {
        Mockito.when(repository.queryFirst(Mockito.isA(Specification.class))).thenReturn(Optional.empty());
        TagServiceImpl service = new TagServiceImpl(repository);

        assertThrows(EntityNotFoundException.class, () -> service.findById(NON_EXISTING_ID));
    }

    @Test
    void testFindAllShouldReturnDtoForAllTags() {
        List<Tag> expected = List.of(
                FIRST_TAG,
                SECOND_TAG
        );

        Mockito.when(repository.query(Mockito.isA(Specification.class), Mockito.eq(Pageable.unpaged())))
                .thenReturn(new PageImpl<>(expected));
        TagServiceImpl service = new TagServiceImpl(repository);

        Page<Tag> actual = service.findAll(Pageable.unpaged());

        assertEquals(expected, actual.getContent());
    }

    @Test
    void testAddShouldAddTagToRepositoryIfNameIsUnique() {
        Mockito.when(repository.add(Mockito.isA(Tag.class))).thenReturn(FIRST_TAG);
        TagServiceImpl service = new TagServiceImpl(repository);

        Tag actual = service.add(FIRST_TAG);

        Mockito.verify(repository, Mockito.times(1)).add(FIRST_TAG);
        assertEquals(FIRST_TAG, actual);
    }

    @Test
    void testAddShouldThrowExceptionIfNameIsUsed() {
        Mockito.when(repository.queryFirst(Mockito.isA(Specification.class))).thenReturn(Optional.of(FIRST_TAG));
        TagServiceImpl service = new TagServiceImpl(repository);

        assertThrows(EntityAlreadyExistsException.class, () -> service.add(FIRST_TAG));
    }

    @Test
    void testRemoveShouldReturnRemovedEntityWhenTagIsRemoved() {
        Mockito.when(repository.remove(FIRST_TAG_ID)).thenReturn(Optional.of(FIRST_TAG));
        TagServiceImpl service = new TagServiceImpl(repository);

        Tag actual = service.remove(FIRST_TAG_ID);

        assertEquals(FIRST_TAG, actual);
        Mockito.verify(repository, Mockito.times(1)).remove(FIRST_TAG_ID);
    }

    @Test
    void testRemoveShouldThrowExceptionIfTagNotExists() {
        Mockito.when(repository.remove(FIRST_TAG_ID)).thenReturn(Optional.empty());
        TagServiceImpl service = new TagServiceImpl(repository);

        assertThrows(EntityNotFoundException.class, () -> service.remove(FIRST_TAG_ID));
        Mockito.verify(repository, Mockito.times(1)).remove(FIRST_TAG_ID);
    }
}
