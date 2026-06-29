package com.chefstack.api.repository;

import com.chefstack.api.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, Long> {

    List<Receta> findByCategoria_Id(Long categoriaId);

    // busca por nombre, ignora mayusculas
    List<Receta> findByNombreContainingIgnoreCase(String texto);
}
