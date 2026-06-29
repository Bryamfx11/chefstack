package com.chefstack.api.service.impl;

import com.chefstack.api.dto.FavoritoResponse;
import com.chefstack.api.exception.ResourceNotFoundException;
import com.chefstack.api.mapper.RecetaMapper;
import com.chefstack.api.model.Favorito;
import com.chefstack.api.model.Receta;
import com.chefstack.api.repository.FavoritoRepository;
import com.chefstack.api.repository.RecetaRepository;
import com.chefstack.api.service.FavoritoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final RecetaRepository recetaRepository;
    private final RecetaMapper recetaMapper;

    public FavoritoServiceImpl(FavoritoRepository favoritoRepository, RecetaRepository recetaRepository,
                               RecetaMapper recetaMapper) {
        this.favoritoRepository = favoritoRepository;
        this.recetaRepository = recetaRepository;
        this.recetaMapper = recetaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoritoResponse> listar(String firebaseUid) {
        return favoritoRepository.findByFirebaseUid(firebaseUid).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FavoritoResponse agregar(String firebaseUid, Long recetaId) {
        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", recetaId));

        // si ya existe, lo devuelve
        Favorito favorito = favoritoRepository.findByFirebaseUidAndReceta_Id(firebaseUid, recetaId)
                .orElseGet(() -> favoritoRepository.save(
                        Favorito.builder().firebaseUid(firebaseUid).receta(receta).build()));

        return toResponse(favorito);
    }

    @Override
    public void eliminar(String firebaseUid, Long recetaId) {
        Favorito favorito = favoritoRepository.findByFirebaseUidAndReceta_Id(firebaseUid, recetaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario no tiene esa receta en favoritos"));
        favoritoRepository.delete(favorito);
    }

    private FavoritoResponse toResponse(Favorito favorito) {
        return new FavoritoResponse(favorito.getId(), favorito.getCreadoEn(),
                recetaMapper.toResponse(favorito.getReceta()));
    }
}
