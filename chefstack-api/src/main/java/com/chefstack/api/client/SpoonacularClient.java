package com.chefstack.api.client;

import com.chefstack.api.client.dto.GuessNutritionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

// nutricion via Spoonacular; si falla o no hay key devuelve Optional vacio
@Component
public class SpoonacularClient implements NutricionClient {

    private static final Logger log = LoggerFactory.getLogger(SpoonacularClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public SpoonacularClient(RestTemplate restTemplate,
                             @Value("${spoonacular.base-url}") String baseUrl,
                             @Value("${spoonacular.api-key:}") String apiKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public Optional<Integer> estimarCalorias(String nombreReceta) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Spoonacular sin API key: se omite el enriquecimiento nutricional");
            return Optional.empty();
        }
        try {
            // guessNutrition: estima por el titulo
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/recipes/guessNutrition")
                    .queryParam("title", nombreReceta)
                    .queryParam("apiKey", apiKey)
                    .toUriString();

            GuessNutritionResponse respuesta = restTemplate.getForObject(url, GuessNutritionResponse.class);
            if (respuesta != null && respuesta.calories() != null && respuesta.calories().value() != null) {
                return Optional.of((int) Math.round(respuesta.calories().value()));
            }
            return Optional.empty();
        } catch (Exception ex) {
            log.warn("No se pudo obtener nutricion de Spoonacular para '{}': {}", nombreReceta, ex.getMessage());
            return Optional.empty();
        }
    }
}
