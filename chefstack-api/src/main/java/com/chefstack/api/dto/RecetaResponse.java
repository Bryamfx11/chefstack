package com.chefstack.api.dto;

import com.chefstack.api.model.Dificultad;

import java.util.List;

public record RecetaResponse(
        Long id,
        String nombre,
        String descripcion,
        String instrucciones,
        Integer tiempoPreparacion,
        Dificultad dificultad,
        Integer porciones,
        String imagenUrl,
        Integer calorias,
        CategoriaResumenResponse categoria,
        List<IngredienteResponse> ingredientes
) {
}
