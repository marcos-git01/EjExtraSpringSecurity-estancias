package com.estancias.controladores;

import com.estancias.entidades.Casa;
import com.estancias.excepciones.MiException;
import com.estancias.servicios.CasaServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/casa") //localhost:8080/casa
public class CasaControlador {
    
    @Autowired
    private CasaServicio casaServicio;
    
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/registrar") //localhost:8080/cliente/registrar
    public String registrar() {
        
        return "casa_form.html";

    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String calle,
            @RequestParam Integer numero, @RequestParam String codPostal,
            @RequestParam String ciudad, @RequestParam String pais,
            @RequestParam("fechaDesde") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde, 
            
            @RequestParam("fechaHasta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            
            @RequestParam Integer minDias, @RequestParam Integer maxDias,
            @RequestParam Double precio, @RequestParam String tipoVivienda,
            ModelMap modelo, MultipartFile archivo) {

        try {

            casaServicio.registrar(archivo, calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda); // si todo sale bien retornamos al index

            modelo.put("exito", "La Casa fue cargada correctamente!");

        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());

            return "casa_form.html"; //volvemos a cargar el formulario.
        }

        return "casa_form.html";
    }
    
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/lista") //localhost:8080/casa/lista
    public String listar(ModelMap modelo) {

        List<Casa> casas = casaServicio.listarCasas();

        modelo.addAttribute("casas", casas);

        return "casa_list.html";

    }
    
    @GetMapping("/modificar/{id}") //localhost:8080/cliente/modificar/{id}
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("casa", casaServicio.getOne(id));
       
        return "casa_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(MultipartFile archivo, @PathVariable String id, String calle, Integer numero, String codPostal, String ciudad, String pais, @RequestParam("fechaDesde") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde, @RequestParam("fechaHasta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta, Integer minDias, Integer maxDias, Double precio, String tipoVivienda, ModelMap modelo) {

        try {

            casaServicio.modificar(archivo, id, calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);

            //Ver esta linea si funciona?
            modelo.put("exito", "La Casa fue modificada correctamente!");

            return "redirect:../lista";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "casa_modificar.html";
        }

    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
   
        try {

            casaServicio.eliminarCasa(id);

            return "redirect:../lista";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            //return "noticia_eliminar.html";
            return "redirect:../lista";
        }

    }
    

}
