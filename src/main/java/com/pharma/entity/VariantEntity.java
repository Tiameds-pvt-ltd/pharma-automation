package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pharma_variant")
public class VariantEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "variant_id", updatable = false, nullable = false, unique = true)
    private UUID variantId;

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date")
    private LocalDate modifiedDate;

    @PrePersist
    public void generateUUID() {
        if (variantId == null) {
            variantId = UUID.randomUUID();
        }
    }

    @OneToMany(mappedBy = "variantEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnitEntity> unitEntities = new ArrayList<>();}
