package com.unexcoder.biblioteca.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unexcoder.biblioteca.entidades.Autor;
import com.unexcoder.biblioteca.entidades.Editorial;
import com.unexcoder.biblioteca.entidades.Libro;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.repositorios.AutorRepositorio;
import com.unexcoder.biblioteca.repositorios.EditorialRepositorio;
import com.unexcoder.biblioteca.repositorios.LibroRepositorio;

@Service
public class LibroServicio {
    
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearLibro( Long isbn, String titulo, UUID idAutor, UUID idEditorial, int ejemplares ) throws ValidationException {
        validar(titulo, idAutor, idEditorial, isbn, ejemplares);
        Optional<Autor> autor = autorRepositorio.findById(idAutor);
        Optional<Editorial> editorial = editorialRepositorio.findById(idEditorial);
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        if (autor.isPresent()) {
            Autor a = autor.get();
            libro.setAutor(a);
        }
        if (editorial.isPresent()) {
            Editorial e = editorial.get();
            libro.setEditorial(e);
        }
        libroRepositorio.save(libro);
    }
    
    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {
        List<Libro> libros = new ArrayList<>();
        libros = libroRepositorio.findAll();
        return libros;
    }

    @Transactional
    public void modificarLibro(Long isbn, String titulo, UUID idAutor, UUID idEditorial, int ejemplares) throws ValidationException {
        validar(titulo, idAutor, idEditorial, isbn, ejemplares);
        Optional<Libro> libro = libroRepositorio.findById(isbn);
        
        if (libro.isPresent()) {
            Libro l = libro.get();
            l.setTitulo(titulo);
            l.setEjemplares(ejemplares);
            Optional<Autor> autor = autorRepositorio.findById(idAutor);
            if (autor.isPresent()) {
                Autor a = autor.get();
                l.setAutor(a);
            }
            Optional<Editorial> editorial = editorialRepositorio.findById(idEditorial);
            if (editorial.isPresent()) {
                Editorial e = editorial.get();
                l.setEditorial(e);
            }
            libroRepositorio.save(l);
        }
    }

    @Transactional(readOnly = true)
    public Libro getOne(Long isbn) {
        return libroRepositorio.getReferenceById(isbn);
    }

    public void validar(String titulo, UUID idAutor, UUID idEditorial, Long isbn, int ejemplares) throws ValidationException {
        if (titulo.isEmpty() || titulo == null) {
            throw new ValidationException("El campo 'titulo' no puede estar vacío");
        }
        if (idEditorial == null) {
            throw new ValidationException("El campo 'editorial' debe ser válido");
        }
        if (idAutor == null) {
            throw new ValidationException("El campo 'autor' debe ser válido");
        }
        if (isbn == null) {
            throw new ValidationException("El campo 'isbn' debe ser válido");
        }
        if (ejemplares <= 0) {
            throw new ValidationException("El campo 'ejemplares' debe ser válido");
        }
    }
}
