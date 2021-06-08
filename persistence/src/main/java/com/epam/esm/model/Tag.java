package com.epam.esm.model;

import com.epam.esm.audit.EntityActionListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tag")
@EntityListeners(EntityActionListener.class)
public class Tag implements Model {
    private static final int HASH_CODE = 61;

    @Id
    @SequenceGenerator(name = "tag_id_seq", sequenceName = "tag_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_seq")
    private Long id;
    @Column(name = "name", length = 64, nullable = false, unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id)
                && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return HASH_CODE;
    }
}
