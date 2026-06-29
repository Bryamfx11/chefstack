package com.chefstack.api.repository;

import com.chefstack.api.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    List<Ingrediente> findByReceta_Id(Long recetaId);
}
