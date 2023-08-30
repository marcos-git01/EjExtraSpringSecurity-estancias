package com.estancias.controladores;

import com.estancias.entidades.Cliente;
import com.estancias.entidades.Usuario;
import com.estancias.excepciones.MiException;
import com.estancias.servicios.ClienteServicio;
import com.estancias.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/lista") //localhost:8080/cliente/lista
    public String listar(ModelMap modelo) {

        List<Cliente> clientes = clienteServicio.listarClientes();

        modelo.addAttribute("clientes", clientes);

        return "cliente_list.html";

    }
    
    @GetMapping("/modificar/{id}") //localhost:8080/cliente/modificar/{id}
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("cliente", clienteServicio.getOne(id));

        List<Usuario> usuarios = usuarioServicio.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        return "cliente_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais, String email, String idUsuario, ModelMap modelo) {

        try {

            clienteServicio.actualizar(id, nombre, calle, numero, codPostal, ciudad, pais, email, idUsuario);

            //Ver esta linea si funciona?
            modelo.put("exito", "El Cliente fue modificado correctamente!");

            return "redirect:../lista";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "cliente_modificar.html";
        }

    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
   
        try {

            clienteServicio.eliminarCliente(id);

            return "redirect:../lista";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            //return "noticia_eliminar.html";
            return "redirect:../lista";
        }

    }
    

}
