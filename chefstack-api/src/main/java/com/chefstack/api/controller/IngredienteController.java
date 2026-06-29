package com.chefstack.api.controller;

import com.chefstack.api.dto.IngredienteRequest;
import com.chefstack.api.dto.IngredienteResponse;
import com.chefstack.api.service.IngredienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@Tag(name = "Ingredientes", description = "CRUD de ingredientes asociados a recetas")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @GetMapping
    @Operation(summary = "Lista los ingredientes de una receta (?recetaId=)")
    public List<IngredienteResponse> listarPorReceta(@RequestParam Long recetaId) {
        return ingredienteService.listarPorReceta(recetaId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un ingrediente por id")
    public IngredienteResponse obtener(@PathVariable Long id) {
        return ingredienteService.obtener(id);
    }

    @PostMapping
    @Operation(summary = "Crea un ingrediente para una receta (requiere autenticacion)")
    public ResponseEntity<IngredienteResponse> crear(@Valid @RequestBody IngredienteRequest request) {
        IngredienteResponse creado = ingredienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un ingrediente (requiere autenticacion)")
    public IngredienteResponse actualizar(@PathVariable Long id, @Valid @RequestBody IngredienteRequest request) {
        return ingredienteService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un ingrediente (requiere autenticacion)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ingredienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
