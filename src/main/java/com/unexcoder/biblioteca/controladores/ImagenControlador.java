package com.unexcoder.biblioteca.controladores;

import org.springframework.http.HttpHeaders;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.biblioteca.entidades.Usuario;
import com.unexcoder.biblioteca.servicios.UsuarioServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("perfil/{id}")
    public ResponseEntity<byte[]> imagenUser(@PathVariable UUID id) {
        Usuario user = usuarioServicio.getOne(id);
        if (user == null || user.getImagen() == null || user.getImagen().getContenido() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] img = user.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or IMAGE_PNG if applicable
        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }
}
