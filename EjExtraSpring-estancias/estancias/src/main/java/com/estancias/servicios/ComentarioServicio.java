
package com.estancias.servicios;

import com.estancias.entidades.Casa;
import com.estancias.entidades.Comentario;
import com.estancias.excepciones.MiException;
import com.estancias.repositorios.CasaRepositorio;
import com.estancias.repositorios.ComentarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioServicio {
    
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    
    @Autowired
    private CasaRepositorio casaRepositorio;
    
    @Transactional
    public void registrar(String descripcion, String idCasa) throws MiException {

        validar(descripcion, idCasa);

        Casa casa = casaRepositorio.findById(idCasa).get();

        Comentario comentario = new Comentario();

        comentario.setDescripcion(descripcion);
        

        comentario.setCasa(casa);

        comentarioRepositorio.save(comentario);
    }
    
    @Transactional
    public void actualizar(String idComentario, String descripcion, String idCasa) throws MiException {

        validar(descripcion, idCasa);

        Optional<Comentario> respuesta = comentarioRepositorio.findById(idComentario);

        if (respuesta.isPresent()) {

            Comentario comentario = respuesta.get();

            comentario.setDescripcion(descripcion);
            

            if (idCasa != null) {

                Optional<Casa> respuestaCasa = casaRepositorio.findById(idCasa);

                if (respuestaCasa.isPresent()) {

                    Casa casa;

                    casa = respuestaCasa.get();

                    comentario.setCasa(casa);

                }

            }

            comentarioRepositorio.save(comentario);

        }

    }
    
    @Transactional
    public void eliminarComentario(String id) throws MiException {

        if (id.isEmpty() || id == null) {
            throw new MiException("El Id del Comentario no puede ser nulo o estar vacio");
        }

        Optional<Comentario> respuesta = comentarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            comentarioRepositorio.delete(respuesta.get());
        }
    }
    
    public Comentario getOne(String id) {
        return comentarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Comentario> listarComentarios() {

        List<Comentario> comentarios = new ArrayList();

        comentarios = comentarioRepositorio.findAll();

        return comentarios;
    }
    
    
    private void validar(String descripcion, String idCasa) throws MiException {

        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiException("La descripcion no puede ser nula o estar vacía");
        }
        
        if (idCasa.isEmpty() || idCasa == null) {
            throw new MiException("El idCasa no puede ser nulo o estar vacío");
        }

    }
    
}
