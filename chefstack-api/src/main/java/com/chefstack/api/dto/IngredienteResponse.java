package com.chefstack.api.dto;

public record IngredienteResponse(
        Long id,
        String nombre,
        String cantidad,
        String unidad,
        Long recetaId
) {
}
