package com.chefstack.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

// imagen subida por el usuario, guardada en la BD
@Entity
@Table(name = "imagenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenSubida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String contentType;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] datos;

    @Column(nullable = false)
    private Instant creadoEn;

    @PrePersist
    void onCreate() {
        this.creadoEn = Instant.now();
    }
}
