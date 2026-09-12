package com.pharmacy.pms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String position;

    @Builder.Default
    private boolean active = true;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // linked login account, optional
}
