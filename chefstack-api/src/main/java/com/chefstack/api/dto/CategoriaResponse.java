package com.chefstack.api.dto;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        int cantidadRecetas
) {
}
