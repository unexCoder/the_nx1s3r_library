package com.unexcoder.biblioteca.controladores;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.servicios.UsuarioServicio;

@Controller
@RequestMapping("/perfil")    
public class UsuarioControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;

    // editar perfil
    @GetMapping("editar/{id}")
    public String editar(@PathVariable String id, ModelMap model) {
        // UUID autorlId = UUID.fromString(id);
        model.put("usuario",usuarioServicio.getOne(UUID.fromString(id)));
        return "editar.html";
    }

    @PostMapping("editar/{id}")
    public String editar(@RequestParam String id, @RequestParam String nombre, @RequestParam String email, 
            @RequestParam(required = true) String password, @RequestParam(required = true) String passwordRepeat, @RequestParam MultipartFile file, ModelMap model) throws ValidationException, Exception {
                try {
                    usuarioServicio.actualizar(UUID.fromString(id), nombre, email, password, passwordRepeat, file);     
                    model.put("exito","Usuario editado correctamente");
                    return "redirect:/inicio";
                } catch (Exception e) {
                    model.put("usuario",usuarioServicio.getOne(UUID.fromString(id)));
                    model.put("error",e.getMessage());
                    model.put("nombre",nombre);
                    model.put("email",email);
                    model.put("password",password);
                    return "editar.html";  
                }
    }
}
