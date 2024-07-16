package com.alura.literatura.repository;

import com.alura.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.anoMorte > :ano AND a.anoNascimento <= :ano")
    List<Autor> buscarAutoresAte(int ano);

    @Query("SELECT a FROM Autor a WHERE a.nome ILIKE %:nome%")
    Optional<Autor> findByNome(String nome);

    @Query("SELECT a FROM Autor a WHERE LOWER(a.nome) = LOWER (:nome)")
    Autor buscarAutor(String nome);

}
