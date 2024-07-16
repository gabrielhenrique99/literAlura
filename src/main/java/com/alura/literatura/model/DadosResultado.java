package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DadosResultado(List<DadosLivro> dadosLivro) {
}

