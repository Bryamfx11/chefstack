package com.chefstack.api.service;

import com.chefstack.api.dto.RecetaRequest;
import com.chefstack.api.dto.RecetaResponse;

import java.util.List;

public interface RecetaService {

    // filtra por categoria y/o nombre (opcionales)
    List<RecetaResponse> listar(Long categoriaId, String buscar);

    RecetaResponse obtener(Long id);

    RecetaResponse crear(RecetaRequest request);

    RecetaResponse actualizar(Long id, RecetaRequest request);

    void eliminar(Long id);
}
