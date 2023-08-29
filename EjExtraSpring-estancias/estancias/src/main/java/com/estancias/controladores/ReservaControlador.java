package com.estancias.controladores;

import com.estancias.entidades.Casa;
import com.estancias.entidades.Cliente;
import com.estancias.entidades.Usuario;
import com.estancias.excepciones.MiException;
import com.estancias.servicios.CasaServicio;
import com.estancias.servicios.ClienteServicio;
import com.estancias.servicios.ReservaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reserva") //localhost:8080/reserva
public class ReservaControlador {

    @Autowired
    private ReservaServicio reservaServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private CasaServicio casaServicio;

    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/registrar") //localhost:8080/cliente/registrar
    public String registrar(ModelMap modelo) {

        List<Cliente> clientes = clienteServicio.listarClientes();

        List<Casa> casas = casaServicio.listarCasas();

        modelo.addAttribute("clientes", clientes);

        modelo.addAttribute("casas", casas);

        return "reserva_form.html";

    }

    @PostMapping("/registro")
    public String registro(@RequestParam String huesped,
            @RequestParam String idCasa, @RequestParam String idCliente,
            ModelMap modelo) {

        try {

            reservaServicio.registrar(huesped, idCasa, idCliente); // si todo sale bien retornamos al index

            modelo.put("exito", "La Reserva fue cargada correctamente!");

        } catch (MiException ex) {

            //Esto es necesario, porque cuando sale el mensaje de error necesito volver a cargar los clientes y casas
            List<Cliente> clientes = clienteServicio.listarClientes();

            List<Casa> casas = casaServicio.listarCasas();

            modelo.addAttribute("clientes", clientes);

            modelo.addAttribute("casas", casas);

            modelo.put("error", ex.getMessage());

            return "reserva_form.html"; //volvemos a cargar el formulario.
        }

        return "reserva_form.html";
    }

}
