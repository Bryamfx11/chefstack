package com.chefstack.api.dto;

import java.time.Instant;

public record FavoritoResponse(
        Long id,
        Instant creadoEn,
        RecetaResponse receta
) {
}
