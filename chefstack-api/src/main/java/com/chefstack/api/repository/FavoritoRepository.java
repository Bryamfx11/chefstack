package com.chefstack.api.repository;

import com.chefstack.api.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// consultas acotadas por el uid del usuario
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByFirebaseUid(String firebaseUid);

    Optional<Favorito> findByFirebaseUidAndReceta_Id(String firebaseUid, Long recetaId);

    boolean existsByFirebaseUidAndReceta_Id(String firebaseUid, Long recetaId);

    // borra los favoritos de una receta (al eliminar la receta)
    void deleteByReceta_Id(Long recetaId);
}
