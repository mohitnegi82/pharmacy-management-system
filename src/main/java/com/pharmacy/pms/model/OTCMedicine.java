package com.pharmacy.pms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "otc_medicines")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OTCMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Disease / symptom category e.g. Fever, Pain Relief, Cold & Cough, Allergy, Digestive
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer minAge;

    @Column(nullable = false)
    private Integer maxAge;

    @Column(nullable = false)
    private String dosage;

    @Column(length = 1000)
    private String notes; // side effects / warnings

    // lower number = higher priority (ranked first among top 3)
    @Builder.Default
    private Integer priority = 5;
}
