package com.chefstack.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// ingrediente anidado dentro de una receta (sin recetaId)
public record IngredienteEnRecetaRequest(

        @NotBlank(message = "El nombre del ingrediente es obligatorio")
        @Size(max = 100)
        String nombre,

        @Size(max = 50)
        String cantidad,

        @Size(max = 30)
        String unidad
) {
}
