package com.chefstack.api.service;

import com.chefstack.api.dto.CategoriaRequest;
import com.chefstack.api.dto.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    List<CategoriaResponse> listar();

    CategoriaResponse obtener(Long id);

    CategoriaResponse crear(CategoriaRequest request);

    CategoriaResponse actualizar(Long id, CategoriaRequest request);

    void eliminar(Long id);
}
