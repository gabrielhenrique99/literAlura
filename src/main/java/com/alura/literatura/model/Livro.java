package com.alura.literatura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alura.literatura.service.ConverterDados;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores = new ArrayList<>();

    private String idioma;
    private Integer quantidadeDowloads;

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.autores = dadosLivro.autores().stream()
                .map(Autor::new)
                .collect(Collectors.toList());
        this.idioma = dadosLivro.idiomas().get(0);
        this.quantidadeDowloads = dadosLivro.quantidadeDownloads();
    }

    @Override
    public String toString(){
        return "{"+"\nTitulo: " + titulo +
                "\nIdioma: " + ConverterDados.converterAbreviacao(idioma) +
                "\nDownloads: " + quantidadeDowloads + "\n}";
    }
}
