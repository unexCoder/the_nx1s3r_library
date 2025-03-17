package com.unexcoder.biblioteca.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unexcoder.biblioteca.entidades.Autor;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.servicios.AutorServicio;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level; 
import java.util.logging.Logger;


@Controller
@RequestMapping("/autor")               // map host/autor
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar")           // map host/autor/registrar
    public String registrar(ModelMap model) {
        List<Autor> autores = autorServicio.listarAutores();
        model.addAttribute("autores", autores);
        model.put("form","autor");
        return "form.html";
    }

    @PostMapping("/registro")           
    public String registro(@RequestParam String nombre, ModelMap model) {
        model.put("form","autor");
        try {
            autorServicio.crearAutor(nombre);    // llamo a mi servicio para persistir        
            List<Autor> autores = autorServicio.listarAutores();
            model.addAttribute("autores", autores);
    
            return "form.html";
        } catch (ValidationException ex) {          
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            model.put("error",ex.getMessage());
            List<Autor> autores = autorServicio.listarAutores();
            model.addAttribute("autores", autores);
            return "form.html";
        }        
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);
        return "list.html";
    }

     // editar registro
    @GetMapping("modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap model) {
        UUID autorlId = UUID.fromString(id);
        model.put("autor",autorServicio.getOne(autorlId));
        return "editar.html";
    }

    @PostMapping("modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap model) {
        try {
            UUID autorlId = UUID.fromString(id);
            autorServicio.modificarAutor(nombre, autorlId);
            return "redirect:../lista";

        } catch (ValidationException e) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, e);
            model.put("autor",autorServicio.getOne(UUID.fromString(id)));
            model.put("error", e.getMessage());
            return "editar.html";
        }
    }
}
