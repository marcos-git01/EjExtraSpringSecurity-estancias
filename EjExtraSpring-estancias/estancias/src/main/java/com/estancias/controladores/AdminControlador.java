package com.estancias.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") //Con el @RequestMapping ingresamos a la url /admin, a todo lo que tiene esta clase
public class AdminControlador {

    @GetMapping("/dashboard") //Con /dashboard accedemos al metodo panelAdminstrativo, que no retorna panel.html
    public String panelAdministrativo() {
        return "panel.html";
    }

}
