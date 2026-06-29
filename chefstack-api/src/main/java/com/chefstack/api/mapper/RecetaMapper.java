package com.chefstack.api.mapper;

import com.chefstack.api.dto.IngredienteResponse;
import com.chefstack.api.dto.RecetaResponse;
import com.chefstack.api.model.Receta;
import org.springframework.stereotype.Component;

import java.util.List;

// receta -> dto
@Component
public class RecetaMapper {

    private final CategoriaMapper categoriaMapper;
    private final IngredienteMapper ingredienteMapper;

    public RecetaMapper(CategoriaMapper categoriaMapper, IngredienteMapper ingredienteMapper) {
        this.categoriaMapper = categoriaMapper;
        this.ingredienteMapper = ingredienteMapper;
    }

    public RecetaResponse toResponse(Receta receta) {
        List<IngredienteResponse> ingredientes = receta.getIngredientes().stream()
                .map(ingredienteMapper::toResponse)
                .toList();

        return new RecetaResponse(
                receta.getId(),
                receta.getNombre(),
                receta.getDescripcion(),
                receta.getInstrucciones(),
                receta.getTiempoPreparacion(),
                receta.getDificultad(),
                receta.getPorciones(),
                receta.getImagenUrl(),
                receta.getCalorias(),
                categoriaMapper.toResumen(receta.getCategoria()),
                ingredientes
        );
    }
}
