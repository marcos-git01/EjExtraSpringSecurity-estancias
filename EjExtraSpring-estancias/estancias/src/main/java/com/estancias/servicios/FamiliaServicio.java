package com.estancias.servicios;

import com.estancias.entidades.Casa;
import com.estancias.entidades.Familia;
import com.estancias.entidades.Usuario;
import com.estancias.excepciones.MiException;
import com.estancias.repositorios.CasaRepositorio;
import com.estancias.repositorios.FamiliaRepositorio;
import com.estancias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamiliaServicio {

    @Autowired
    private FamiliaRepositorio familiaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CasaRepositorio casaRepositorio;

    @Transactional
    public void registrar(String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String email, String idUsuario, String idCasa) throws MiException {

        validar(nombre, edadMin, edadMax, numHijos, email, idUsuario, idCasa);

        Usuario usuario = usuarioRepositorio.findById(idUsuario).get();

        Casa casa = casaRepositorio.findById(idCasa).get();

        Familia familia = new Familia();

        familia.setNombre(nombre);
        familia.setEdadMin(edadMin);
        familia.setEdadMax(edadMax);
        familia.setNumHijos(numHijos);
        familia.setEmail(email);

        familia.setUsuario(usuario);

        familia.setCasa(casa);

        familiaRepositorio.save(familia);
    }

    @Transactional
    public void actualizar(String idFamilia, String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String email, String idUsuario, String idCasa) throws MiException {

        validar(nombre, edadMin, edadMax, numHijos, email, idUsuario, idCasa);

        Optional<Familia> respuesta = familiaRepositorio.findById(idFamilia);

        if (respuesta.isPresent()) {

            Familia familia = respuesta.get();

            familia.setNombre(nombre);
            familia.setEdadMin(edadMin);
            familia.setEdadMax(edadMax);
            familia.setNumHijos(numHijos);
            familia.setEmail(email);

            if (idUsuario != null) {

                Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(idUsuario);

                if (respuestaUsuario.isPresent()) {

                    Usuario usuario;

                    usuario = respuestaUsuario.get();

                    familia.setUsuario(usuario);

                }

            }

            if (idCasa != null) {

                Optional<Casa> respuestaCasa = casaRepositorio.findById(idCasa);

                if (respuestaCasa.isPresent()) {

                    Casa casa;

                    casa = respuestaCasa.get();

                    familia.setCasa(casa);

                }

            }

            familiaRepositorio.save(familia);

        }

    }

    @Transactional
    public void eliminarFamilia(String id) throws MiException {

        if (id.isEmpty() || id == null) {
            throw new MiException("El Id de la Familia no puede ser nulo o estar vacio");
        }

        Optional<Familia> respuesta = familiaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            familiaRepositorio.delete(respuesta.get());
        }
    }

    public Familia getOne(String id) {
        return familiaRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Familia> listarFamilias() {

        List<Familia> familias = new ArrayList();

        familias = familiaRepositorio.findAll();

        return familias;
    }

    private void validar(String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String email, String idUsuario, String idCasa) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }

        if (edadMin < 0 || edadMin == null) {
            throw new MiException("La edad minima no puede ser nula o ser menor a 0");
        }

        if (edadMax < 0 || edadMax == null) {
            throw new MiException("La edad maxima no puede ser nula o ser menor a 0");
        }

        if (numHijos == null) {
            throw new MiException("El numero de hijos no puede ser nulo");
        }

        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo o estar vacio");
        }

        if (idUsuario.isEmpty() || idUsuario == null) {
            throw new MiException("El idUsuario no puede ser nulo o estar vacío");
        }

        if (idCasa.isEmpty() || idCasa == null) {
            throw new MiException("El idCasa no puede ser nulo o estar vacío");
        }

    }

}
