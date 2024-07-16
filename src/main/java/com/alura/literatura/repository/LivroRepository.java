package com.alura.literatura.repository;

import com.alura.literatura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("SELECT l FROM Livro l WHERE l.idioma = :idioma")
    List<Livro> buscarLivroPorIdioma(String idioma);

    @Query("SELECT l FROM Livro l WHERE l.titulo ILIKE %:titulo%")
    Optional<Livro> findByTitulo(String titulo);

    List<Livro> findTop10ByOrderByQuantidadeDowloadsDesc();

}
