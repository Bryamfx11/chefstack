package com.chefstack.api.mapper;

import com.chefstack.api.dto.CategoriaRequest;
import com.chefstack.api.dto.CategoriaResponse;
import com.chefstack.api.dto.CategoriaResumenResponse;
import com.chefstack.api.model.Categoria;
import org.springframework.stereotype.Component;

// categoria <-> dto
@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaRequest req) {
        return Categoria.builder()
                .nombre(req.nombre())
                .descripcion(req.descripcion())
                .build();
    }

    // vuelca los datos sobre una entidad existente
    public void updateEntity(Categoria categoria, CategoriaRequest req) {
        categoria.setNombre(req.nombre());
        categoria.setDescripcion(req.descripcion());
    }

    public CategoriaResponse toResponse(Categoria categoria) {
        int cantidad = categoria.getRecetas() == null ? 0 : categoria.getRecetas().size();
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(),
                categoria.getDescripcion(), cantidad);
    }

    public CategoriaResumenResponse toResumen(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        return new CategoriaResumenResponse(categoria.getId(), categoria.getNombre());
    }
}
