package com.estancias.controladores;

import com.estancias.excepciones.MiException;
import com.estancias.servicios.CasaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam Integer minDias, @RequestParam Integer maxDias,
            @RequestParam Double precio, @RequestParam String tipoVivienda,
            ModelMap modelo) {

        try {

            casaServicio.registrar(calle, numero, codPostal, ciudad, pais, minDias, maxDias, precio, tipoVivienda); // si todo sale bien retornamos al index

            modelo.put("exito", "La Casa fue cargada correctamente!");

        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());

            return "casa_form.html"; //volvemos a cargar el formulario.
        }

        return "casa_form.html";
    }

}