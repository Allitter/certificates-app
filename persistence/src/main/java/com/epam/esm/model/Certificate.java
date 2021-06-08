package com.epam.esm.model;

import com.epam.esm.audit.EntityActionListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "certificate")
@EntityListeners(EntityActionListener.class)
public class Certificate implements Model {
    private static final int HASH_CODE = 13;

    @Id
    @SequenceGenerator(name = "certificate_id_seq", sequenceName = "certificate_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "description", length = 255, nullable = false)
    private String description;
    @Column(name = "price", nullable = false)
    private Integer price;
    @Column(name = "duration", nullable = false)
    private Integer duration;
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDate createDate;
    @Column(name = "last_update_date", nullable = false)
    private LocalDate lastUpdateDate;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "certificate_tag",
            joinColumns = {@JoinColumn(name = "id_certificate")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")}
    )
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();
    @Column(name = "removed", columnDefinition = "boolean default false")
    private boolean removed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Certificate)) return false;
        Certificate that = (Certificate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return HASH_CODE;
    }
}
