package com.chefstack.api.mapper;

import com.chefstack.api.dto.IngredienteEnRecetaRequest;
import com.chefstack.api.dto.IngredienteRequest;
import com.chefstack.api.dto.IngredienteResponse;
import com.chefstack.api.model.Ingrediente;
import com.chefstack.api.model.Receta;
import org.springframework.stereotype.Component;

// ingrediente <-> dto
@Component
public class IngredienteMapper {

    public Ingrediente toEntity(IngredienteRequest req, Receta receta) {
        return Ingrediente.builder()
                .nombre(req.nombre())
                .cantidad(req.cantidad())
                .unidad(req.unidad())
                .receta(receta)
                .build();
    }

    // desde la version anidada en la receta
    public Ingrediente toEntity(IngredienteEnRecetaRequest req, Receta receta) {
        return Ingrediente.builder()
                .nombre(req.nombre())
                .cantidad(req.cantidad())
                .unidad(req.unidad())
                .receta(receta)
                .build();
    }

    public IngredienteResponse toResponse(Ingrediente ingrediente) {
        Long recetaId = ingrediente.getReceta() != null ? ingrediente.getReceta().getId() : null;
        return new IngredienteResponse(ingrediente.getId(), ingrediente.getNombre(),
                ingrediente.getCantidad(), ingrediente.getUnidad(), recetaId);
    }
}
