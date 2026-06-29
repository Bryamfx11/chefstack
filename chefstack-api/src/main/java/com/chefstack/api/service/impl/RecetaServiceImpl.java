package com.chefstack.api.service.impl;

import com.chefstack.api.client.NutricionClient;
import com.chefstack.api.dto.RecetaRequest;
import com.chefstack.api.dto.RecetaResponse;
import com.chefstack.api.exception.ResourceNotFoundException;
import com.chefstack.api.mapper.IngredienteMapper;
import com.chefstack.api.mapper.RecetaMapper;
import com.chefstack.api.model.Categoria;
import com.chefstack.api.model.Ingrediente;
import com.chefstack.api.model.Receta;
import com.chefstack.api.repository.CategoriaRepository;
import com.chefstack.api.repository.FavoritoRepository;
import com.chefstack.api.repository.RecetaRepository;
import com.chefstack.api.service.RecetaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;
    private final CategoriaRepository categoriaRepository;
    private final FavoritoRepository favoritoRepository;
    private final RecetaMapper recetaMapper;
    private final IngredienteMapper ingredienteMapper;
    private final NutricionClient nutricionClient;

    public RecetaServiceImpl(RecetaRepository recetaRepository, CategoriaRepository categoriaRepository,
                             FavoritoRepository favoritoRepository, RecetaMapper recetaMapper,
                             IngredienteMapper ingredienteMapper, NutricionClient nutricionClient) {
        this.recetaRepository = recetaRepository;
        this.categoriaRepository = categoriaRepository;
        this.favoritoRepository = favoritoRepository;
        this.recetaMapper = recetaMapper;
        this.ingredienteMapper = ingredienteMapper;
        this.nutricionClient = nutricionClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaResponse> listar(Long categoriaId, String buscar) {
        List<Receta> recetas;
        if (categoriaId != null) {
            recetas = recetaRepository.findByCategoria_Id(categoriaId);
        } else if (StringUtils.hasText(buscar)) {
            recetas = recetaRepository.findByNombreContainingIgnoreCase(buscar.trim());
        } else {
            recetas = recetaRepository.findAll();
        }
        return recetas.stream().map(recetaMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecetaResponse obtener(Long id) {
        return recetaMapper.toResponse(buscarOFallar(id));
    }

    @Override
    public RecetaResponse crear(RecetaRequest request) {
        Categoria categoria = buscarCategoria(request.categoriaId());

        Receta receta = Receta.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .instrucciones(request.instrucciones())
                .tiempoPreparacion(request.tiempoPreparacion())
                .dificultad(request.dificultad())
                .porciones(request.porciones())
                .imagenUrl(request.imagenUrl())
                .categoria(categoria)
                .build();

        agregarIngredientes(receta, request);

        // calorias estimadas de la api
        nutricionClient.estimarCalorias(receta.getNombre()).ifPresent(receta::setCalorias);

        return recetaMapper.toResponse(recetaRepository.save(receta));
    }

    @Override
    public RecetaResponse actualizar(Long id, RecetaRequest request) {
        Receta receta = buscarOFallar(id);
        boolean cambioNombre = !receta.getNombre().equalsIgnoreCase(request.nombre());

        receta.setNombre(request.nombre());
        receta.setDescripcion(request.descripcion());
        receta.setInstrucciones(request.instrucciones());
        receta.setTiempoPreparacion(request.tiempoPreparacion());
        receta.setDificultad(request.dificultad());
        receta.setPorciones(request.porciones());
        receta.setImagenUrl(request.imagenUrl());
        receta.setCategoria(buscarCategoria(request.categoriaId()));

        // reemplaza ingredientes (orphanRemoval borra los viejos)
        receta.getIngredientes().clear();
        agregarIngredientes(receta, request);

        // recalcula calorias solo si cambio el nombre
        if (cambioNombre) {
            nutricionClient.estimarCalorias(receta.getNombre()).ifPresent(receta::setCalorias);
        }

        return recetaMapper.toResponse(receta);
    }

    @Override
    public void eliminar(Long id) {
        Receta receta = buscarOFallar(id);
        // quita la receta de los favoritos de todos antes de borrarla (evita violar la FK)
        favoritoRepository.deleteByReceta_Id(id);
        recetaRepository.delete(receta);
    }

    // agrega los ingredientes anidados
    private void agregarIngredientes(Receta receta, RecetaRequest request) {
        if (request.ingredientes() == null) {
            return;
        }
        for (var ingredienteReq : request.ingredientes()) {
            Ingrediente ingrediente = ingredienteMapper.toEntity(ingredienteReq, receta);
            receta.getIngredientes().add(ingrediente);
        }
    }

    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", categoriaId));
    }

    private Receta buscarOFallar(Long id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", id));
    }
}
