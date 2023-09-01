
package com.estancias.controladores;

import com.estancias.entidades.Usuario;
import com.estancias.excepciones.MiException;
import com.estancias.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired //Una instancia unica de UsuarioServicio para poder utilizar sus metodos
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }
    
    @PostMapping("/registro") // Este metodo recibe todos los valores que nos envia el formulario registro.html
    public String registro(@RequestParam String alias, @RequestParam String email, @RequestParam String clave,
            String clave2, ModelMap modelo) { //@RequestParam significa parametro requerido y ModelMap modelo para poder interactuar con el html

        try {
            usuarioServicio.registrar(alias, email, clave, clave2);

            modelo.put("exito", "Usuario registrado correctamente!"); // Si se registra con exito, va a lanzar un mensaje de exito a traves del modelo 

            //return "index.html"; //retornando al index no muestra el mensaje de exito
            return "registro.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage()); // Si no se registra lanza un mensaje de error, el mensaje de las validaciones
            modelo.put("alias", alias);
            modelo.put("email", email);

            return "registro.html";
        }

    }
    
    //Al ingresar al /login podia o no, venir un error, por eso el @RequestParam(required = false), no esta requerido
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o Clave invalidos!");
        }

        return "login.html";
    }
    
    //Para poder ingresar /inicio y al metodo interno String inicio, 
    //necesito estar logueado como USER o ADMIN, mediante @PreAuthorize
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) { //Este metodo recibe un objeto del tipo HttpSession

        //Este usuario logueado, contiene todos los datos de la session
        Usuario logueado = (Usuario) session.getAttribute("usuariosession"); //"usuariosession" es la llave, que contiene al usuario que abrio la sesion en el sistema

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "inicio.html";
    }
    
    
    
}
