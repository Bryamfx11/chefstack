package com.chefstack.api.controller;

import com.chefstack.api.exception.BadRequestException;
import com.chefstack.api.exception.ResourceNotFoundException;
import com.chefstack.api.model.ImagenSubida;
import com.chefstack.api.repository.ImagenRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

// subida y servido de imagenes de recetas
@RestController
@RequestMapping("/api/uploads")
@Tag(name = "Imagenes", description = "Subida de imagenes de recetas")
public class UploadController {

    private final ImagenRepository imagenRepository;

    public UploadController(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Sube una imagen y devuelve su URL (requiere autenticacion)")
    public ResponseEntity<Map<String, String>> subir(@RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new BadRequestException("El archivo esta vacio");
        }
        String tipo = archivo.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) {
            throw new BadRequestException("El archivo debe ser una imagen");
        }
        try {
            ImagenSubida imagen = imagenRepository.save(ImagenSubida.builder()
                    .contentType(tipo).datos(archivo.getBytes()).build());
            return ResponseEntity.ok(Map.of("url", "/api/uploads/" + imagen.getId()));
        } catch (IOException e) {
            throw new BadRequestException("No se pudo leer el archivo");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Devuelve la imagen por id")
    public ResponseEntity<byte[]> ver(@PathVariable Long id) {
        ImagenSubida imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen", id));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imagen.getContentType()))
                .body(imagen.getDatos());
    }
}
