package com.unexcoder.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unexcoder.biblioteca.entidades.Editorial;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.repositorios.EditorialRepositorio;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearEditorial(String nombre) throws ValidationException {
        validar(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorialRepositorio.save(editorial);
    }

    @Transactional(readOnly = true)
    public List<Editorial> listarEditoriales() {
        List<Editorial> editoriales = new ArrayList<>();
        editoriales = editorialRepositorio.findAll();
        return editoriales;
    }

    @Transactional
    public void modificarEditorial(String nombre, UUID id) throws ValidationException {
        validar(nombre);
        Optional<Editorial> editorial = editorialRepositorio.findById(id);
        if (editorial.isPresent()) {
            Editorial e = editorial.get();
            e.setNombre(nombre);
            editorialRepositorio.save(e);
        }
    }

    public void validar(String nombre) throws ValidationException {
        if (nombre.isEmpty() || nombre == null) {
            throw new ValidationException("El campo 'nombre' no puede estar vacío");
        }
    }

    @Transactional(readOnly = true)
    public Editorial getOne(UUID id) {
        return editorialRepositorio.getReferenceById(id);
    }
}
