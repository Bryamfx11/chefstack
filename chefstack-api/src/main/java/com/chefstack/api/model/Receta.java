package com.chefstack.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recetas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    // en minutos
    private Integer tiempoPreparacion;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Dificultad dificultad;

    private Integer porciones;

    @Column(length = 500)
    private String imagenUrl;

    // calorias de la api (puede ser null)
    private Integer calorias;

    // N:1 con categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // 1:N con ingredientes
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Ingrediente> ingredientes = new ArrayList<>();
}
