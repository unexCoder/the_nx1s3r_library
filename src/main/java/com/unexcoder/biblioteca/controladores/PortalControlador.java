package com.unexcoder.biblioteca.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.biblioteca.entidades.Usuario;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.servicios.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index() {
        // return "index.html";
        return "redirect:/login";
    }
    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, 
            @RequestParam String password, @RequestParam String passwordRepeat, @RequestParam MultipartFile file, ModelMap model) throws Exception {
                try {
                    usuarioServicio.registrar(nombre, email, password, passwordRepeat,file);
                    model.put("exito","Usuario registrado correctamente");
                    return "redirect:/inicio";
                } catch (ValidationException e) {
                    model.put("error",e.getMessage());
                    model.put("nombre",nombre);
                    model.put("email",email);
                    model.put("password",password);
                    return "registro.html";  
                }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
           if (error != null) {
               model.put("error", "Usuario o Contraseña inválidos!");        
            }
           return "login.html";
       }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logged = (Usuario) session.getAttribute("userSesion");
        if (logged.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "inicio.html";
       }
}
