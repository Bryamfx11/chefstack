package com.chefstack.api.service;

import com.chefstack.api.dto.FavoritoResponse;

import java.util.List;

// siempre en el ambito del usuario (uid)
public interface FavoritoService {

    List<FavoritoResponse> listar(String firebaseUid);

    FavoritoResponse agregar(String firebaseUid, Long recetaId);

    void eliminar(String firebaseUid, Long recetaId);
}
