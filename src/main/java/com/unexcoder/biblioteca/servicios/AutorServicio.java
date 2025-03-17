package com.unexcoder.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unexcoder.biblioteca.entidades.Autor;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.repositorios.AutorRepositorio;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws ValidationException {
        validar(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autorRepositorio.save(autor);
    }
    
    @Transactional(readOnly = true)
    public List<Autor> listarAutores() {
        List<Autor> autores = new ArrayList<>();
        autores = autorRepositorio.findAll();
        return autores;
    }
    
    @Transactional
    public void modificarAutor(String nombre, UUID id) throws ValidationException {
        validar(nombre);
        Optional<Autor> autor = autorRepositorio.findById(id);
        if (autor.isPresent()) {
            Autor a = autor.get();
            a.setNombre(nombre);
            autorRepositorio.save(a);
        }
    }

    public void validar(String nombre) throws ValidationException {
        if (nombre.isEmpty() || nombre == null) {
            throw new ValidationException("El campo 'nombre' no puede estar vacío");
        }
    }

    @Transactional(readOnly = true)
    public Autor getOne(UUID id) {
        return autorRepositorio.getReferenceById(id);
    }
}
