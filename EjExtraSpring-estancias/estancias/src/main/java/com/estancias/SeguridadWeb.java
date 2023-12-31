
package com.estancias;

import com.estancias.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter{
    
    @Autowired
    public UsuarioServicio usuarioServicio; //Unica instancia para utilizar sus metodos

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { //Este metodo recibe un objeto del tipo AuthenticationManagerBuilder llamado auth
        // Con el objeto que llega por parametro, lo que vamos hacer es configurar 
        //el manejador de seguridad de Spring Security, y le vamos a decir cual es 
        //el servicio que tiene que utilizar para autenticar un usuario
        
        auth.userDetailsService(usuarioServicio) // userDetailsService utiliza UsuarioServicio para autenticar usuarios mediante el metodo loadUserByUsername(String email)
                .passwordEncoder(new BCryptPasswordEncoder()); // Esto se utiliza para codificar la contraseña mediante BCryptPasswordEncoder()
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests() //Va autorizar determinados parametros, .antMatchers siempre que estemos ingresando a determinadas partes
                    .antMatchers("/admin/*").hasRole("ADMIN") // para ingresar a /admin/*, lo que sea, solo se puede acceder si tenemos rol Admin
                    .antMatchers("/css/*", "/js/*", "/img/*", "/**") // para ingresar a las partes establecidas del sistema permita todos los archivos cuyas rutas definamos
                    .permitAll() // Van a ser permitidos por cualquier persona que acceda al sistema
                .and().formLogin() // Esto pertenece al formulario de login
                    .loginPage("/login") //Esta linea indica donde esta la pagina de login
                    .loginProcessingUrl("/logincheck") //Cual va a ser la url con la cual Spring Security va a autenticar un usuario, para procesar este inicio de sesion la url va a ser /logincheck
                    .usernameParameter("alias") //Configuramos las credenciales, como username el "email"
                    .passwordParameter("clave") //Configuramos las credeniciales, como password "password"
                    .defaultSuccessUrl("/inicio") //Si el login es correcto, se dirige a url /inicio
                    .permitAll()
                .and().logout() //Esto es la salida de nuestro sistema
                    .logoutUrl("/logout") // url para cerrar la sesion. Tanto /logout como /logincheck no necesitan controlador, Spring Security se encarga 
                    .logoutSuccessUrl("/") //logoutSuccessUrl("/") con la / retorna al index, si la sesion se cerro correctamente
                    .permitAll()
                .and().csrf()
                .disable(); //Estas lineas son para que se deshabilite una caracteristicas de Spring Security

    }
    
}
