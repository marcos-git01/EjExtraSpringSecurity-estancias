package com.estancias.servicios;

import com.estancias.entidades.Casa;
import com.estancias.entidades.Cliente;
import com.estancias.entidades.Reserva;
import com.estancias.excepciones.MiException;
import com.estancias.repositorios.CasaRepositorio;
import com.estancias.repositorios.ClienteRepositorio;
import com.estancias.repositorios.ReservaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaServicio {

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Autowired
    private CasaRepositorio casaRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Transactional
    public void registrar(String huesped, String idCasa, String idCliente) throws MiException {

        validar(huesped, idCasa, idCliente);

        Casa casa = casaRepositorio.findById(idCasa).get();

        Cliente cliente = clienteRepositorio.findById(idCliente).get();

        Reserva reserva = new Reserva();

        reserva.setHuesped(huesped);
        
        reserva.setFechaDesde(new Date());

        reserva.setCasa(casa);

        reserva.setCliente(cliente);

        reservaRepositorio.save(reserva);
    }

    @Transactional
    public void actualizar(String idReserva, String huesped, String idCasa, String idCliente) throws MiException {

        validar(huesped, idCasa, idCliente);

        Optional<Reserva> respuesta = reservaRepositorio.findById(idReserva);

        if (respuesta.isPresent()) {

            Reserva reserva = respuesta.get();

            reserva.setHuesped(huesped);

            if (idCasa != null) {

                Optional<Casa> respuestaCasa = casaRepositorio.findById(idCasa);

                if (respuestaCasa.isPresent()) {

                    Casa casa;

                    casa = respuestaCasa.get();

                    reserva.setCasa(casa);

                }

            }

            if (idCliente != null) {

                Optional<Cliente> respuestaCliente = clienteRepositorio.findById(idCliente);

                if (respuestaCliente.isPresent()) {

                    Cliente cliente;

                    cliente = respuestaCliente.get();

                    reserva.setCliente(cliente);

                }

            }

            reservaRepositorio.save(reserva);

        }

    }

    @Transactional
    public void eliminarReserva(String id) throws MiException {

        if (id.isEmpty() || id == null) {
            throw new MiException("El Id de la Reserva no puede ser nulo o estar vacio");
        }

        Optional<Reserva> respuesta = reservaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            reservaRepositorio.delete(respuesta.get());
        }
    }

    public Reserva getOne(String id) {
        return reservaRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarReservas() {

        List<Reserva> reservas = new ArrayList();

        reservas = reservaRepositorio.findAll();

        return reservas;
    }

    private void validar(String huesped, String idCasa, String idCliente) throws MiException {

        if (huesped.isEmpty() || huesped == null) {
            throw new MiException("El huesped no puede ser nulo o estar vacío");
        }

        if (idCasa.isEmpty() || idCasa == null) {
            throw new MiException("El IdCasa no puede ser nulo o estar vacío");
        }

        if (idCliente.isEmpty() || idCliente == null) {
            throw new MiException("El IdCliente no puede ser nulo o estar vacío");
        }

    }

}
