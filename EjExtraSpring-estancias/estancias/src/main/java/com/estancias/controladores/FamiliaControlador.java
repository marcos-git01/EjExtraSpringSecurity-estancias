package com.estancias.controladores;

import com.estancias.entidades.Casa;
import com.estancias.entidades.Familia;
import com.estancias.entidades.Usuario;
import com.estancias.excepciones.MiException;
import com.estancias.servicios.CasaServicio;
import com.estancias.servicios.FamiliaServicio;
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
@RequestMapping("/familia") //localhost:8080/familia
public class FamiliaControlador {

    @Autowired
    private FamiliaServicio familiaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private CasaServicio casaServicio;

    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/registrar") //localhost:8080/familia/registrar
    public String registrar(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.listarUsuarios();

        List<Casa> casas = casaServicio.listarCasas();

        modelo.addAttribute("usuarios", usuarios);

        modelo.addAttribute("casas", casas);

        return "familia_form.html";

    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam Integer edadMin,
            @RequestParam Integer edadMax, @RequestParam Integer numHijos,
            @RequestParam String email, @RequestParam String idUsuario,
            @RequestParam String idCasa, ModelMap modelo) {

        try {

            familiaServicio.registrar(nombre, edadMin, edadMax, numHijos, email, idUsuario, idCasa); // si todo sale bien retornamos al index

            modelo.put("exito", "La Familia fue cargada correctamente!");

        } catch (MiException ex) {

            //Esto es necesario, porque cuando sale el mensaje de error necesito volver a cargar los usuarios
            List<Usuario> usuarios = usuarioServicio.listarUsuarios();

            modelo.addAttribute("usuarios", usuarios);

            List<Casa> casas = casaServicio.listarCasas();

            modelo.addAttribute("casas", casas);

            modelo.put("error", ex.getMessage());

            return "familia_form.html"; //volvemos a cargar el formulario.
        }

        return "familia_form.html";
    }

    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/lista") //localhost:8080/familia/lista
    public String listar(ModelMap modelo) {

        List<Familia> familias = familiaServicio.listarFamilias();

        modelo.addAttribute("familias", familias);

        return "familia_list.html";

    }

    @GetMapping("/modificar/{id}") //localhost:8080/familia/modificar/{id}
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("familia", familiaServicio.getOne(id));

        List<Usuario> usuarios = usuarioServicio.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        List<Casa> casas = casaServicio.listarCasas();

        modelo.addAttribute("casas", casas);

        return "familia_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String email, String idUsuario, String idCasa, ModelMap modelo) {

        try {

            familiaServicio.actualizar(id, nombre, edadMin, edadMax, numHijos, email, idUsuario, idCasa);

            //Ver esta linea si funciona?
            modelo.put("exito", "La familia fue modificada correctamente!");

            return "redirect:../lista";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "familia_modificar.html";
        }

    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
   
        try {

            familiaServicio.eliminarFamilia(id);

            return "redirect:../lista";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            //return "noticia_eliminar.html";
            return "redirect:../lista";
        }

    }
    

}
