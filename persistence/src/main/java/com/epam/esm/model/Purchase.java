package com.epam.esm.model;

import com.epam.esm.audit.EntityActionListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchase")
@EntityListeners(EntityActionListener.class)
public class Purchase implements Model {
    private static final int HASH_CODE = 3;

    @Id
    @SequenceGenerator(name = "purchase_id_seq", sequenceName = "purchase_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_id_seq")
    private Long id;
    @Column(name = "cost", nullable = false)
    private Integer cost;
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Builder.Default
    private List<PurchaseCertificate> purchaseCertificates = new ArrayList<>();
    @Column(name = "removed", columnDefinition = "boolean default false")
    private boolean removed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(id, purchase.id);
    }

    @Override
    public int hashCode() {
        return HASH_CODE;
    }
}
