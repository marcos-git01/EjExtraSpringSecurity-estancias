package com.estancias.controladores;

import com.estancias.entidades.Usuario;
import com.estancias.excepciones.MiException;
import com.estancias.servicios.ClienteServicio;
import com.estancias.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cliente") //localhost:8080/cliente
public class ClienteControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/registrar") //localhost:8080/cliente/registrar
    public String registrar(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        return "cliente_form.html";

    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String calle,
            @RequestParam Integer numero, @RequestParam String codPostal,
            @RequestParam String ciudad, @RequestParam String pais,
            @RequestParam String email,
            @RequestParam String idUsuario, ModelMap modelo) {

        try {

            clienteServicio.registrar(nombre, calle, numero, codPostal, ciudad, pais, email, idUsuario); // si todo sale bien retornamos al index

            modelo.put("exito", "El Cliente fue cargado correctamente!");

        } catch (MiException ex) {

            //Esto es necesario, porque cuando sale el mensaje de error necesito volver a cargar los usuarios
            List<Usuario> usuarios = usuarioServicio.listarUsuarios();

            modelo.addAttribute("usuarios", usuarios);

            modelo.put("error", ex.getMessage());

            return "cliente_form.html"; //volvemos a cargar el formulario.
        }

        return "cliente_form.html";
    }

}
