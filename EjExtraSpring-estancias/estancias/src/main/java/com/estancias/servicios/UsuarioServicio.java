package com.estancias.servicios;

import com.estancias.entidades.Usuario;
import com.estancias.enumeraciones.Rol;
import com.estancias.excepciones.MiException;
import com.estancias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(String alias, String email, String clave, String clave2) throws MiException {

        validar(alias, email, clave, clave2);

        Usuario usuario = new Usuario();

        usuario.setAlias(alias);
        usuario.setEmail(email);

        //usuario.setClave(clave);

        // Cuando se setea la contraseña al usuario tambien la vamos a codificar con BCryptPasswordEncoder()
        usuario.setClave(new BCryptPasswordEncoder().encode(clave));
                     
        usuario.setFechaAlta(new Date());
        
        usuario.setRol(Rol.USER);

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

            //usuario.setClave(clave);

            // Cuando se setea la contraseña al usuario tambien la vamos a codificar con BCryptPasswordEncoder()
            usuario.setClave(new BCryptPasswordEncoder().encode(clave));
                                   
            usuario.setFechaAlta(usuario.getFechaAlta());
            
            usuario.setRol(usuario.getRol());

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
    
    @Transactional
    public void cambiarRol(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.USER)) {

                usuario.setRol(Rol.ADMIN);

            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USER);
            }
        }
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
    
    // Este metodo se utiliza para autenticar un usuario de nuestro dominio y 
    // transformarlo en un usuario del dominio de Spring Security
    @Override
    public UserDetails loadUserByUsername(String alias) throws UsernameNotFoundException { 

        Usuario usuario = usuarioRepositorio.buscarPorAlias(alias);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString()); //ROLE_USER por ejemplo

            permisos.add(p);
            
            //Vamos a interceptar en este punto, ya sabemos que el usuario ingreso a la plataforma y le dimos los permisos
            //Vamos atrapar ese usuario, que ya esta autenticado y guardarlo en la sesion web
            //Esta llamada lo que hace, es recuperar los atributos del Request, de la solicitud http
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes(); 
            
            //Y esos atributos los guardamos en el objeto session de la interface HttpSession
            HttpSession session = attr.getRequest().getSession(true); // y de los atributos queremos la session, .getSession(true)
            
            //Y en esta session, vamos a seter los atributos, mediante la llave "usuariosession", 
            //va a viajar los valores del usuario que habiamos buscado al principio en la primer linea del metodo
            session.setAttribute("usuariosession", usuario); 

            return new User(usuario.getAlias(), usuario.getClave(), permisos);
        } else {
            return null;
        }
    }
    
}
