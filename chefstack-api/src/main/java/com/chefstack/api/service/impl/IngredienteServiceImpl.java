package com.chefstack.api.service.impl;

import com.chefstack.api.dto.IngredienteRequest;
import com.chefstack.api.dto.IngredienteResponse;
import com.chefstack.api.exception.ResourceNotFoundException;
import com.chefstack.api.mapper.IngredienteMapper;
import com.chefstack.api.model.Ingrediente;
import com.chefstack.api.model.Receta;
import com.chefstack.api.repository.IngredienteRepository;
import com.chefstack.api.repository.RecetaRepository;
import com.chefstack.api.service.IngredienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IngredienteServiceImpl implements IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final RecetaRepository recetaRepository;
    private final IngredienteMapper ingredienteMapper;

    public IngredienteServiceImpl(IngredienteRepository ingredienteRepository,
                                  RecetaRepository recetaRepository, IngredienteMapper ingredienteMapper) {
        this.ingredienteRepository = ingredienteRepository;
        this.recetaRepository = recetaRepository;
        this.ingredienteMapper = ingredienteMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredienteResponse> listarPorReceta(Long recetaId) {
        // 404 si la receta no existe
        if (!recetaRepository.existsById(recetaId)) {
            throw new ResourceNotFoundException("Receta", recetaId);
        }
        return ingredienteRepository.findByReceta_Id(recetaId).stream()
                .map(ingredienteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public IngredienteResponse obtener(Long id) {
        return ingredienteMapper.toResponse(buscarOFallar(id));
    }

    @Override
    public IngredienteResponse crear(IngredienteRequest request) {
        Receta receta = buscarReceta(request.recetaId());
        Ingrediente guardado = ingredienteRepository.save(ingredienteMapper.toEntity(request, receta));
        return ingredienteMapper.toResponse(guardado);
    }

    @Override
    public IngredienteResponse actualizar(Long id, IngredienteRequest request) {
        Ingrediente ingrediente = buscarOFallar(id);
        ingrediente.setNombre(request.nombre());
        ingrediente.setCantidad(request.cantidad());
        ingrediente.setUnidad(request.unidad());
        // permite moverlo a otra receta
        ingrediente.setReceta(buscarReceta(request.recetaId()));
        return ingredienteMapper.toResponse(ingrediente);
    }

    @Override
    public void eliminar(Long id) {
        ingredienteRepository.delete(buscarOFallar(id));
    }

    private Receta buscarReceta(Long recetaId) {
        return recetaRepository.findById(recetaId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", recetaId));
    }

    private Ingrediente buscarOFallar(Long id) {
        return ingredienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente", id));
    }
}
