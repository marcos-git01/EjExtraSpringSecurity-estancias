package com.estancias.repositorios;

import com.estancias.entidades.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstanciaRepositorio extends JpaRepository<Estancia, String> {

}
