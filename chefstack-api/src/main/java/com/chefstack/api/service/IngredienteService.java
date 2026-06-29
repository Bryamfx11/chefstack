package com.chefstack.api.service;

import com.chefstack.api.dto.IngredienteRequest;
import com.chefstack.api.dto.IngredienteResponse;

import java.util.List;

public interface IngredienteService {

    List<IngredienteResponse> listarPorReceta(Long recetaId);

    IngredienteResponse obtener(Long id);

    IngredienteResponse crear(IngredienteRequest request);

    IngredienteResponse actualizar(Long id, IngredienteRequest request);

    void eliminar(Long id);
}
