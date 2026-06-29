package com.chefstack.api.dto;

import com.chefstack.api.model.Dificultad;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record RecetaRequest(

        @NotBlank(message = "El nombre de la receta es obligatorio")
        @Size(max = 120)
        String nombre,

        @Size(max = 500)
        String descripcion,

        String instrucciones,

        @Positive(message = "El tiempo de preparacion debe ser mayor a 0")
        Integer tiempoPreparacion,

        @NotNull(message = "La dificultad es obligatoria")
        Dificultad dificultad,

        @Positive(message = "Las porciones deben ser mayores a 0")
        Integer porciones,

        @Size(max = 500)
        String imagenUrl,

        @NotNull(message = "La receta debe pertenecer a una categoria (categoriaId)")
        Long categoriaId,

        @Valid
        List<IngredienteEnRecetaRequest> ingredientes
) {
}
