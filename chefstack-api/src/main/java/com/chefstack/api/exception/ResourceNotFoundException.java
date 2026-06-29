package com.chefstack.api.exception;

// -> 404
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String recurso, Object id) {
        super("%s no encontrado con id %s".formatted(recurso, id));
    }

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
