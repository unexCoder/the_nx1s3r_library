package com.unexcoder.biblioteca.servicios;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.biblioteca.entidades.Imagen;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.repositorios.ImagenRepositorio;

@Service
public class ImagenServicio {
    
    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @Transactional
    public Imagen guardarImagen(MultipartFile file) throws ValidationException{
        if (file != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(file.getContentType());
                imagen.setNombre(file.getName());
                imagen.setContenido(file.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.out.println("error" + e.getMessage());
            }
        }
        return null;
    } 

    @Transactional
    public Imagen actualizarImagen(UUID id,MultipartFile file)  throws Exception{
        if (file != null) {
            try {
                Imagen imagen = new Imagen();
                if (id != null) {
                    Optional<Imagen> img = imagenRepositorio.findById(id);
                    if (img.isPresent()) {
                        imagen = img.get();
                    }
                }
                imagen.setMime(file.getContentType());
                imagen.setNombre(file.getName());
                imagen.setContenido(file.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.out.println("error" + e.getMessage());
            }
        }
        return null;
    } 
}
