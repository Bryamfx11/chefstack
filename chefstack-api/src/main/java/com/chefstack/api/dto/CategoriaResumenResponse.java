package com.chefstack.api.dto;

// categoria reducida para incrustar en una receta
public record CategoriaResumenResponse(
        Long id,
        String nombre
) {
}
