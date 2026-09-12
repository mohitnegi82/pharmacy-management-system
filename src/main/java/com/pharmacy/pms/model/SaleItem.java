package com.pharmacy.pms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sale_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity;
    private Double unitPrice;
}
