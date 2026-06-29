package com.chefstack.api.exception;

import java.time.Instant;
import java.util.Map;

// formato uniforme de error para el cliente
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String mensaje,
        String ruta,
        Map<String, String> erroresCampos
) {
    public static ApiError of(int status, String error, String mensaje, String ruta) {
        return new ApiError(Instant.now(), status, error, mensaje, ruta, null);
    }
}
