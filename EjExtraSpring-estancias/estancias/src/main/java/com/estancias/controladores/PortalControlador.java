
package com.estancias.controladores;

import com.estancias.excepciones.MiException;
import com.estancias.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
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
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }

        return "login.html";
    }
    
    
    
}
