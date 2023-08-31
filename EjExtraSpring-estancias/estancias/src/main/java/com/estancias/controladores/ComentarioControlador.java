package com.estancias.controladores;

import com.estancias.entidades.Casa;
import com.estancias.entidades.Comentario;
import com.estancias.excepciones.MiException;
import com.estancias.servicios.CasaServicio;
import com.estancias.servicios.ComentarioServicio;
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
@RequestMapping("/comentario") //localhost:8080/comentario
public class ComentarioControlador {

    @Autowired
    private CasaServicio casaServicio;

    @Autowired
    private ComentarioServicio comentarioServicio;

    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/registrar") //localhost:8080/comentario/registrar
    public String registrar(ModelMap modelo) {

        List<Casa> casas = casaServicio.listarCasas();

        modelo.addAttribute("casas", casas);

        return "comentario_form.html";

    }

    @PostMapping("/registro")
    public String registro(@RequestParam String descripcion,
            @RequestParam String idCasa, ModelMap modelo) {

        try {

            comentarioServicio.registrar(descripcion, idCasa); // si todo sale bien retornamos al index

            modelo.put("exito", "El Comentario fue cargado correctamente!");

        } catch (MiException ex) {

            //Esto es necesario, porque cuando sale el mensaje de error necesito volver a cargar las casas
            List<Casa> casas = casaServicio.listarCasas();

            modelo.addAttribute("casas", casas);

            modelo.put("error", ex.getMessage());

            return "comentario_form.html"; //volvemos a cargar el formulario.
        }

        return "comentario_form.html";
    }

    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/lista") //localhost:8080/comentario/lista
    public String listar(ModelMap modelo) {

        List<Comentario> comentarios = comentarioServicio.listarComentarios();

        modelo.addAttribute("comentarios", comentarios);

        return "comentario_list.html";

    }

    @GetMapping("/modificar/{id}") //localhost:8080/comentario/modificar/{id}
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("comentario", comentarioServicio.getOne(id));

        List<Casa> casas = casaServicio.listarCasas();

        modelo.addAttribute("casas", casas);

        return "comentario_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String descripcion, String idCasa, ModelMap modelo) {

        try {

            comentarioServicio.actualizar(id, descripcion, idCasa);

            //Ver esta linea si funciona?
            modelo.put("exito", "El Comentario fue modificado correctamente!");

            return "redirect:../lista";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "comentario_modificar.html";
        }

    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
   
        try {

            comentarioServicio.eliminarComentario(id);

            return "redirect:../lista";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            //return "noticia_eliminar.html";
            return "redirect:../lista";
        }

    }

}
