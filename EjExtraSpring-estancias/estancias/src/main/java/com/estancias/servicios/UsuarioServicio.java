package com.estancias.servicios;

import com.estancias.entidades.Usuario;
import com.estancias.excepciones.MiException;
import com.estancias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(String alias, String email, String clave, String clave2) throws MiException {

        validar(alias, email, clave, clave2);

        Usuario usuario = new Usuario();

        usuario.setAlias(alias);
        usuario.setEmail(email);

        usuario.setClave(clave);

        // Cuando se setea la contraseña al usuario tambien la vamos a codificar con BCryptPasswordEncoder()
        //usuario.setPassword(new BCryptPasswordEncoder().encode(password)); 
        usuario.setFechaAlta(new Date());

        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void actualizar(String idUsuario, String alias, String email, String clave, String clave2) throws MiException {

        validar(alias, email, clave, clave2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setAlias(alias);
            usuario.setEmail(email);

            usuario.setClave(clave);

            // Cuando se setea la contraseña al usuario tambien la vamos a codificar con BCryptPasswordEncoder()
            //usuario.setPassword(new BCryptPasswordEncoder().encode(password)); 
            usuario.setFechaAlta(usuario.getFechaAlta());

            usuarioRepositorio.save(usuario);

        }

    }

    @Transactional
    public void eliminarUsuario(String id) throws MiException {

        if (id.isEmpty() || id == null) {
            throw new MiException("El Id del Usuario no puede ser nulo o estar vacio");
        }

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            usuarioRepositorio.delete(respuesta.get());
        }
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;
    }

    private void validar(String alias, String email, String clave, String clave2) throws MiException {

        if (alias.isEmpty() || alias == null) {
            throw new MiException("El alias no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo o estar vacio");
        }
        if (clave.isEmpty() || clave == null || clave.length() <= 5) {
            throw new MiException("La clave no puede estar vacía, y debe tener más de 5 dígitos");
        }

        if (!clave.equals(clave2)) {
            throw new MiException("Las claves ingresadas deben ser iguales");
        }

    }

}
