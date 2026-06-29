package com.chefstack.api.controller;

import com.chefstack.api.dto.FavoritoResponse;
import com.chefstack.api.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// siempre sobre los favoritos del usuario (uid)
@RestController
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos", description = "Recetas favoritas del usuario autenticado")
@SecurityRequirement(name = "bearer-jwt")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @GetMapping
    @Operation(summary = "Lista las recetas favoritas del usuario actual")
    public List<FavoritoResponse> listar(@AuthenticationPrincipal String uid) {
        return favoritoService.listar(uid);
    }

    @PostMapping("/{recetaId}")
    @Operation(summary = "Agrega una receta a favoritos")
    public ResponseEntity<FavoritoResponse> agregar(@AuthenticationPrincipal String uid,
                                                    @PathVariable Long recetaId) {
        FavoritoResponse favorito = favoritoService.agregar(uid, recetaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(favorito);
    }

    @DeleteMapping("/{recetaId}")
    @Operation(summary = "Quita una receta de favoritos")
    public ResponseEntity<Void> eliminar(@AuthenticationPrincipal String uid, @PathVariable Long recetaId) {
        favoritoService.eliminar(uid, recetaId);
        return ResponseEntity.noContent().build();
    }
}
