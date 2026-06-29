package com.chefstack.api.controller;

import com.chefstack.api.dto.RecetaRequest;
import com.chefstack.api.dto.RecetaResponse;
import com.chefstack.api.service.RecetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@Tag(name = "Recetas", description = "CRUD de recetas con filtro por categoria y busqueda por nombre")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping
    @Operation(summary = "Lista recetas; admite filtros opcionales ?categoriaId= y ?buscar=")
    public List<RecetaResponse> listar(@RequestParam(required = false) Long categoriaId,
                                       @RequestParam(required = false) String buscar) {
        return recetaService.listar(categoriaId, buscar);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una receta por id con sus ingredientes")
    public RecetaResponse obtener(@PathVariable Long id) {
        return recetaService.obtener(id);
    }

    @PostMapping
    @Operation(summary = "Crea una receta (requiere autenticacion)")
    public ResponseEntity<RecetaResponse> crear(@Valid @RequestBody RecetaRequest request) {
        RecetaResponse creada = recetaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una receta (requiere autenticacion)")
    public RecetaResponse actualizar(@PathVariable Long id, @Valid @RequestBody RecetaRequest request) {
        return recetaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una receta (requiere autenticacion)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
