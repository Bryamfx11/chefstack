package com.chefstack.api.client;

import java.util.Optional;

// proveedor de info nutricional (intercambiable)
public interface NutricionClient {

    // calorias por nombre; vacio si no hay dato
    Optional<Integer> estimarCalorias(String nombreReceta);
}
