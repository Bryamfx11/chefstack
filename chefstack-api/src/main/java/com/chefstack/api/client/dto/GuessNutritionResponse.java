package com.chefstack.api.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// respuesta de guessNutrition (solo lo que usamos)
@JsonIgnoreProperties(ignoreUnknown = true)
public record GuessNutritionResponse(Valor calories) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Valor(Double value, String unit) {
    }
}
