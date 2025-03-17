package com.unexcoder.biblioteca.entidades;

import java.util.UUID;

import com.unexcoder.biblioteca.enumeraciones.LoginRol;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    private UUID id;

    private String nombre;
    private String email;
    private String password;

    @Enumerated (EnumType.STRING)
    private  LoginRol rol;

    @OneToOne(cascade = CascadeType.ALL, optional = true) 
    private Imagen imagen;    
}
