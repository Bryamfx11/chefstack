package com.chefstack.api.repository;

import com.chefstack.api.model.ImagenSubida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<ImagenSubida, Long> {
}
