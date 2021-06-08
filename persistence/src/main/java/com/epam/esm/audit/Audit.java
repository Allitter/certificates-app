package com.epam.esm.audit;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false, updatable = false)
    private Operation operation;
    @Column(name = "time", updatable = false, nullable = false)
    private LocalDateTime time;
    @Column(name = "audit_content", updatable = false, length = 999999)
    private String auditContent;

    protected Audit() {
    }

    private Audit(Operation operation, LocalDateTime time, String auditContent) {
        this.operation = operation;
        this.time = time;
        this.auditContent = auditContent;
    }

    public static Audit persistFor(Object object) {
        return new Audit(Operation.PERSIST, LocalDateTime.now(), object.toString());
    }

    public static Audit updateFor(Object object) {
        return new Audit(Operation.UPDATE, LocalDateTime.now(), object.toString());
    }

    public static Audit removeFor(Object object) {
        return new Audit(Operation.REMOVE, LocalDateTime.now(), object.toString());
    }

    public enum Operation {
        PERSIST,
        UPDATE,
        REMOVE
    }
}
