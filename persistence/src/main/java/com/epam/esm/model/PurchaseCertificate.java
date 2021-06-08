package com.epam.esm.model;

import com.epam.esm.audit.EntityActionListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.StringJoiner;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchase_certificate")
@EntityListeners(EntityActionListener.class)
public class PurchaseCertificate implements Model {
    private static final int HASH_CODE = 14;
    @Id
    @SequenceGenerator(name = "purchase_certificate_id_seq", sequenceName = "purchase_certificate_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_certificate_id_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_certificate")
    private Certificate certificate;
    @ManyToOne
    @JoinColumn(name = "id_purchase")
    private Purchase purchase;
    @Column(name = "cnt", nullable = false)
    private int count;

    @Override
    public String toString() {
        return new StringJoiner(", ", PurchaseCertificate.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("certificate=" + certificate)
                .add("count=" + count)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseCertificate)) return false;
        PurchaseCertificate that = (PurchaseCertificate) o;
        return Objects.equals(id, that.id)
                && Objects.equals(certificate, that.certificate)
                && Objects.equals(purchase, that.purchase);
    }

    @Override
    public int hashCode() {
        return HASH_CODE;
    }
}
