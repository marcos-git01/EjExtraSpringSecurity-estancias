package com.estancias.servicios;

import com.estancias.entidades.Cliente;
import com.estancias.entidades.Usuario;
import com.estancias.excepciones.MiException;
import com.estancias.repositorios.ClienteRepositorio;
import com.estancias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais, String email, String idUsuario) throws MiException {

        validar(nombre, calle, numero, codPostal, ciudad, pais, email, idUsuario);

        Usuario usuario = usuarioRepositorio.findById(idUsuario).get();

        Cliente cliente = new Cliente();

        cliente.setNombre(nombre);
        cliente.setCalle(calle);
        cliente.setNumero(numero);
        cliente.setCodPostal(codPostal);
        cliente.setCiudad(ciudad);
        cliente.setPais(pais);
        cliente.setEmail(email);

        cliente.setUsuario(usuario);

        clienteRepositorio.save(cliente);
    }

    @Transactional
    public void actualizar(String idCliente, String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais, String email, String idUsuario) throws MiException {

        validar(nombre, calle, numero, codPostal, ciudad, pais, email, idUsuario);

        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);

        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();

            cliente.setNombre(nombre);
            cliente.setCalle(calle);
            cliente.setNumero(numero);
            cliente.setCodPostal(codPostal);
            cliente.setCiudad(ciudad);
            cliente.setPais(pais);
            cliente.setEmail(email);

            if (idUsuario != null) {

                Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(idUsuario);

                if (respuestaUsuario.isPresent()) {

                    Usuario usuario;

                    usuario = respuestaUsuario.get();

                    cliente.setUsuario(usuario);

                }

            }

            clienteRepositorio.save(cliente);

        }

    }

    @Transactional
    public void eliminarCliente(String id) throws MiException {

        if (id.isEmpty() || id == null) {
            throw new MiException("El Id del Cliente no puede ser nulo o estar vacio");
        }

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {
            clienteRepositorio.delete(respuesta.get());
        }
    }

    public Cliente getOne(String id) {
        return clienteRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {

        List<Cliente> clientes = new ArrayList();

        clientes = clienteRepositorio.findAll();

        return clientes;
    }

    private void validar(String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais, String email, String idUsuario) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }

        if (calle.isEmpty() || calle == null) {
            throw new MiException("La calle no puede ser nula o estar vacía");
        }

        if (numero < 0 || numero == null) {
            throw new MiException("El numero no puede ser nulo o ser menor a 0");
        }

        if (codPostal.isEmpty() || codPostal == null) {
            throw new MiException("El codigo Postal no puede ser nulo o estar vacío");
        }

        if (ciudad.isEmpty() || ciudad == null) {
            throw new MiException("La ciudad no puede ser nula o estar vacía");
        }

        if (pais.isEmpty() || pais == null) {
            throw new MiException("El pais no puede ser nulo o estar vacío");
        }

        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo o estar vacio");
        }

        if (idUsuario.isEmpty() || idUsuario == null) {
            throw new MiException("El idUsuario no puede ser nulo o estar vacío");
        }

    }

}
