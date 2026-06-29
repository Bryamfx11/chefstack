package com.chefstack.api.service.impl;

import com.chefstack.api.dto.CategoriaRequest;
import com.chefstack.api.dto.CategoriaResponse;
import com.chefstack.api.exception.BadRequestException;
import com.chefstack.api.exception.ResourceNotFoundException;
import com.chefstack.api.mapper.CategoriaMapper;
import com.chefstack.api.model.Categoria;
import com.chefstack.api.repository.CategoriaRepository;
import com.chefstack.api.service.CategoriaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtener(Long id) {
        return categoriaMapper.toResponse(buscarOFallar(id));
    }

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {
        // no duplicar por nombre
        if (categoriaRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new BadRequestException("Ya existe una categoria con el nombre '" + request.nombre() + "'");
        }
        Categoria guardada = categoriaRepository.save(categoriaMapper.toEntity(request));
        return categoriaMapper.toResponse(guardada);
    }

    @Override
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarOFallar(id);
        categoriaMapper.updateEntity(categoria, request);
        return categoriaMapper.toResponse(categoria);
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = buscarOFallar(id);
        // no borrar una categoria con recetas
        if (!categoria.getRecetas().isEmpty()) {
            throw new BadRequestException("No se puede eliminar la categoria porque tiene recetas asociadas");
        }
        categoriaRepository.delete(categoria);
    }

    // 404 si no existe
    private Categoria buscarOFallar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }
}
