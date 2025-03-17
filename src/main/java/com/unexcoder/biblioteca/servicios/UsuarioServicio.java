package com.unexcoder.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.biblioteca.entidades.Imagen;
import com.unexcoder.biblioteca.entidades.Usuario;
import com.unexcoder.biblioteca.enumeraciones.LoginRol;
import com.unexcoder.biblioteca.exepciones.ValidationException;
import com.unexcoder.biblioteca.repositorios.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    private void validar(String nombre, String email, String password, String passwordRepeat, MultipartFile file)
            throws ValidationException {
        if (nombre.isEmpty() || nombre == null) {
            throw new ValidationException("El campo nombre debe ser correcto");
        }
        if (email.isEmpty() || email == null) {
            throw new ValidationException("El campo email ser correcto");
        }
        if (password.isEmpty() || password == null || password.length() < 6) {
            throw new ValidationException("El campo password debe ser correcto");
        }
        if (passwordRepeat.isEmpty() || passwordRepeat == null || !passwordRepeat.equals(password)) {
            throw new ValidationException("El campo password debe coincidir con el anterior");
        }
        if (file.getSize() > 10 * 1024 * 1024) { // 5MB limit
            throw new ValidationException("El archivo no debe ser menor a 10 MB");            
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = usuarioRepositorio.buscarPorEmail(email);
        if (user != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());
            permisos.add(p);

            // load user login context
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("userSesion", user);
            //
            return new User(user.getEmail(), user.getPassword(), permisos);
        } else {
            throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
        }
    }

    @Transactional
    public void registrar(String nombre, String email, String password, String passwordRepeat, MultipartFile file)
            throws Exception {
        validar(nombre, email, password, passwordRepeat,file);
        Usuario newUser = new Usuario();
        newUser.setNombre(nombre);
        newUser.setEmail(email);
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        newUser.setRol(LoginRol.USER);
        // Only process the image if the file is not empty
        
        if (file != null && !file.isEmpty()) {
            Imagen img = imagenServicio.guardarImagen(file);
            newUser.setImagen(img);
        } // If no file, imagen remains null
        usuarioRepositorio.save(newUser);
    }

    @Transactional
    public void actualizar(UUID id, String nombre, String email, String password, String passwordRepeat,
            MultipartFile file) throws Exception {
        validar(nombre, email, password, passwordRepeat,file);
        Optional<Usuario> u = usuarioRepositorio.findById(id);
        if (u.isPresent()) {
            Usuario user = u.get();
            user.setNombre(nombre);
            user.setEmail(email);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            UUID idImagen = null;
            if (user.getImagen() != null) {
                idImagen = user.getImagen().getId();
            }
            Imagen img = imagenServicio.actualizarImagen(idImagen, file);
            user.setImagen(img);
            usuarioRepositorio.save(user);
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional
    public void cambiarRol(UUID id) {
        Optional<Usuario> u = usuarioRepositorio.findById(id);
        if (u.isPresent()) {
            Usuario user = u.get();
            if (user.getRol().equals(LoginRol.ADMIN)) {
                user.setRol(LoginRol.USER);
            } else if (user.getRol().equals(LoginRol.USER)) {
                user.setRol(LoginRol.ADMIN);
            }
        }
    }

    @Transactional(readOnly = true)
    public Usuario getOne(UUID id) {
        Optional<Usuario> u = usuarioRepositorio.findById(id);
        if (u.isPresent()) {
            Usuario user = u.get();
            return user;
        }
        return null;
    }

}
