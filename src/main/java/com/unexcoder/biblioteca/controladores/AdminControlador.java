package com.unexcoder.biblioteca.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.biblioteca.entidades.Usuario;
import com.unexcoder.biblioteca.servicios.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    UsuarioServicio usuarioServicio;
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard.html";
    }

    @GetMapping("dashboard/usuarios")
    public String listar(ModelMap model) {
        List<Usuario> users = usuarioServicio.listarUsuarios();
        model.addAttribute("users", users);
        return "list.html";
    }
    
    @GetMapping("dashboard/usuarios/{id}")
    public String cambiarRol(@PathVariable String id) {
        usuarioServicio.cambiarRol(UUID.fromString(id));
        return "redirect:/admin/dashboard/usuarios";
    }
}
