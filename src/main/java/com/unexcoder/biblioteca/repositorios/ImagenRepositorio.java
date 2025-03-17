package com.unexcoder.biblioteca.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unexcoder.biblioteca.entidades.Imagen;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen,UUID>{
    
}
