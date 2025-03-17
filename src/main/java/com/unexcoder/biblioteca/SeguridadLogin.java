package com.unexcoder.biblioteca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// configuracion de seguridad
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SeguridadLogin {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/admin/**").hasRole("ADMIN") //acceso full admins
            .requestMatchers("/css/", "/js/", "/img/", "/**").permitAll() //acceso sin autenticación a los archivos estáticos (CSS, JS, imágenes)
            .requestMatchers("/login","/registrar").permitAll() //acceso full admins
            .anyRequest().authenticated() // requiere autenticar
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/inicio",true)
                .permitAll()
            )
            .logout((logout) -> logout 
                        .logoutUrl("/logout") 
                        .logoutSuccessUrl("/") 
                        .permitAll())
            .csrf((csrf -> csrf.disable()));  //deshabilita la protección CSRF
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
