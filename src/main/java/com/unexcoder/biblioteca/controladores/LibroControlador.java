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

import com.unexcoder.biblioteca.entidades.Autor;
import com.unexcoder.biblioteca.entidades.Editorial;
import com.unexcoder.biblioteca.entidades.Libro;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.servicios.AutorServicio;
import com.unexcoder.biblioteca.servicios.EditorialServicio;
// import com.unexcoder.biblioteca.servicios.AutorServicio;
// import com.unexcoder.biblioteca.servicios.EditorialServicio;
import com.unexcoder.biblioteca.servicios.LibroServicio;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar") // localhost:8080/libro/registrar
    public String registrar(ModelMap model) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("autores", autores);
        model.addAttribute("editoriales", editoriales);        
        model.put("form","libro");
        
        List<Libro> libros = libroServicio.listarLibros();
        model.addAttribute("libros", libros);        
        return "form.html";
    }

    @PostMapping("/registro") // localhost:8080/libro/registro
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) int ejemplares, @RequestParam(required = false)  String idAutor,
            @RequestParam(required = false) String idEditorial, ModelMap model) {
        model.put("form","libro");
        if (idAutor == null || idAutor.isEmpty() || idAutor == "") {
            model.put("error","Debe seleccionar un autor");
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            model.addAttribute("autores", autores);
            model.addAttribute("editoriales", editoriales);
            return "form.html";
        } else if (idEditorial == null || idEditorial.isEmpty() || idEditorial == "") {
            model.put("error","Debe seleccionar una editorial");
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            model.addAttribute("autores", autores);
            model.addAttribute("editoriales", editoriales);
            return "form.html";
        }
        try {
            UUID autorId = UUID.fromString(idAutor);
            UUID editorialId = UUID.fromString(idEditorial);
            libroServicio.crearLibro(isbn, titulo,autorId,editorialId,ejemplares);
            model.put("exito","El libro ha sido guardado");
            
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            model.addAttribute("autores", autores);
            model.addAttribute("editoriales", editoriales);
            List<Libro> libros = libroServicio.listarLibros();
            model.addAttribute("libros", libros);  
            return "form.html"; // volvemos a cargar el formulario.

        } catch (ValidationException ex) {
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            model.addAttribute("autores", autores);
            model.addAttribute("editoriales", editoriales);
            List<Libro> libros = libroServicio.listarLibros();
            model.addAttribute("libros", libros);                
            // Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, "Error en el registro", ex);
            model.put("error",ex.getMessage());
            return "form.html"; // volvemos a cargar el formulario.
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();
        modelo.addAttribute("libros", libros);
        return "list.html";
    }

     // editar registro
    @GetMapping("modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap model) {       
        model.put("libro",libroServicio.getOne(id));
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("autores", autores);
        model.addAttribute("editoriales", editoriales);   
        return "editar.html";
    }
    
    
    @PostMapping("modificar/{id}")
    public String modificar(@PathVariable @RequestParam(required = false) Long id, @RequestParam(required = false) Long isbn, @RequestParam String titulo, @RequestParam String autorID, @RequestParam String editorialID, @RequestParam(required =  false) int ejemplares, ModelMap model) {
        model.put("form","libro");
        if (autorID == null || autorID.isEmpty() || autorID == "") {
            model.put("libro",libroServicio.getOne(id));
            model.put("error","Debe seleccionar un autor");
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            model.addAttribute("autores", autores);
            model.addAttribute("editoriales", editoriales);
            return "editar.html";
        } else if (editorialID == null || editorialID.isEmpty() || editorialID == "") {
            model.put("libro",libroServicio.getOne(id));
            model.put("error","Debe seleccionar una editorial");
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            model.addAttribute("autores", autores);
            model.addAttribute("editoriales", editoriales);
            return "editar.html";
        }
        try {
            UUID idAutor = UUID.fromString(autorID);
            UUID idEditorial = UUID.fromString(editorialID);
            libroServicio.modificarLibro(isbn, titulo, idAutor, idEditorial, ejemplares);
            return "redirect:../lista";
            
        } catch (ValidationException e) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, e);
            model.put("libro",libroServicio.getOne(id));
            model.put("error", e.getMessage());
            return "editar.html";
        }
    }

    
}
