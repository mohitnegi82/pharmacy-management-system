package com.pharmacy.pms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity;
    private Double unitPrice;
}
