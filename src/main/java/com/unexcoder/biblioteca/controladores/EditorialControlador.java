package com.unexcoder.biblioteca.controladores;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unexcoder.biblioteca.entidades.Editorial;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.servicios.EditorialServicio;

@Controller
@RequestMapping("/editorial") // map host/autor
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("editoriales", editoriales);
        model.put("form","editorial");
        return "form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap model) {
        model.put("form","editorial");
        try {
            editorialServicio.crearEditorial(nombre);
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            model.addAttribute("editoriales", editoriales);
            return "form.html";
        } catch (ValidationException ex) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, ex);
            model.put("error",ex.getMessage());
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            model.addAttribute("editoriales", editoriales);
            return "form.html";
        }
        // return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap model) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("editoriales", editoriales);
        return "list.html";
    }

    // editar registro
    @GetMapping("modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap model) {
        UUID editorialId = UUID.fromString(id);
        model.put("editorial",editorialServicio.getOne(editorialId));
        return "editar.html";
    }
    
    @PostMapping("modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap model) {
        try {
            UUID editoriallId = UUID.fromString(id);
            editorialServicio.modificarEditorial(nombre, editoriallId);
            return "redirect:../lista";
        } catch (ValidationException e) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, e);
            model.put("editorial",editorialServicio.getOne(UUID.fromString(id)));
            model.put("error", e.getMessage());
            return "editar.html";
        }
    }

}
