package com.estancias.servicios;

import com.estancias.entidades.Casa;
import com.estancias.excepciones.MiException;
import com.estancias.repositorios.CasaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CasaServicio {

    @Autowired
    private CasaRepositorio casaRepositorio;

    @Transactional
    public void registrar(String calle, Integer numero, String codPostal, String ciudad, String pais, Integer minDias, Integer maxDias, Double precio, String tipoVivienda) throws MiException {

        validar(calle, numero, codPostal, ciudad, pais, minDias, maxDias, precio, tipoVivienda);

        Casa casa = new Casa();

        casa.setCalle(calle);
        casa.setNumero(numero);
        casa.setCodPostal(codPostal);
        casa.setCiudad(ciudad);
        casa.setPais(pais);

        casa.setFechaDesde(new Date());

        casa.setMinDias(minDias);
        casa.setMaxDias(maxDias);
        casa.setPrecio(precio);
        casa.setTipoVivienda(tipoVivienda);

        casaRepositorio.save(casa);
    }

    @Transactional
    public void actualizar(String idCasa, String calle, Integer numero, String codPostal, String ciudad, String pais, Integer minDias, Integer maxDias, Double precio, String tipoVivienda) throws MiException {

        validar(calle, numero, codPostal, ciudad, pais, minDias, maxDias, precio, tipoVivienda);

        Optional<Casa> respuesta = casaRepositorio.findById(idCasa);

        if (respuesta.isPresent()) {

            Casa casa = respuesta.get();

            casa.setCalle(calle);
            casa.setNumero(numero);
            casa.setCodPostal(codPostal);
            casa.setCiudad(ciudad);
            casa.setPais(pais);

            casa.setFechaDesde(casa.getFechaDesde());

            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);

            casaRepositorio.save(casa);

        }

    }

    @Transactional
    public void eliminarCasa(String id) throws MiException {

        if (id.isEmpty() || id == null) {
            throw new MiException("El Id de la Casa no puede ser nulo o estar vacio");
        }

        Optional<Casa> respuesta = casaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            casaRepositorio.delete(respuesta.get());
        }
    }

    public Casa getOne(String id) {
        return casaRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Casa> listarCasas() {

        List<Casa> casas = new ArrayList();

        casas = casaRepositorio.findAll();

        return casas;
    }

    private void validar(String calle, Integer numero, String codPostal, String ciudad, String pais, Integer minDias, Integer maxDias, Double precio, String tipoVivienda) throws MiException {

        if (calle.isEmpty() || calle == null) {
            throw new MiException("La calle no puede ser nulo o estar vacío");
        }

        if (numero < 0 || numero == null) {
            throw new MiException("El numero no puede ser nulo o ser menor a 0");
        }

        if (codPostal.isEmpty() || codPostal == null) {
            throw new MiException("El codigo postal no puede ser nulo o estar vacio");
        }

        if (ciudad.isEmpty() || ciudad == null) {
            throw new MiException("La ciudad no puede ser nula o estar vacía");
        }

        if (pais.isEmpty() || pais == null) {
            throw new MiException("El pais no puede ser nulo o estar vacío");
        }

        if (minDias < 0 || minDias == null) {
            throw new MiException("El minimo de días no puede ser nulo o ser menor a 0");
        }

        if (maxDias < 0 || maxDias == null) {
            throw new MiException("El maximo de días no puede ser nulo o ser menor a 0");
        }

        if (precio < 0 || precio == null) {
            throw new MiException("El precio no puede ser nulo o ser menor a 0");
        }

        if (tipoVivienda.isEmpty() || tipoVivienda == null) {
            throw new MiException("El tipo de vivienda no puede ser nulo o estar vacío");
        }

    }

}
