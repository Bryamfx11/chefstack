package com.chefstack.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

// vincula un uid de Firebase con una receta; uid + receta es unico
@Entity
@Table(name = "favoritos", uniqueConstraints = @UniqueConstraint(columnNames = {"firebase_uid", "receta_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firebase_uid", nullable = false, length = 128)
    private String firebaseUid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receta_id", nullable = false)
    private Receta receta;

    @Column(nullable = false)
    private Instant creadoEn;

    // fecha de creacion
    @PrePersist
    void onCreate() {
        this.creadoEn = Instant.now();
    }
}
