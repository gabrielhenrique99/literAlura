package com.alura.literatura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "autores")
@Getter
@Setter
@NoArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer anoNascimento;
    private Integer anoMorte;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    public Autor(DadosAutor dadosAutor){
        this.nome = dadosAutor.nome();
        this.anoNascimento = dadosAutor.anoNascimento();
        this.anoMorte = dadosAutor.anoMorte();
    }

    public String toString() {
        return "\nNome: " + nome +
                "\nNascimento: " + anoNascimento +
                "\nFalecimento: " +anoMorte +
                "\nLivros: " + livro;
    }

}
