package com.chefstack.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IngredienteRequest(

        @NotBlank(message = "El nombre del ingrediente es obligatorio")
        @Size(max = 100)
        String nombre,

        @Size(max = 50)
        String cantidad,

        @Size(max = 30)
        String unidad,

        @NotNull(message = "El ingrediente debe pertenecer a una receta (recetaId)")
        Long recetaId
) {
}
