package com.alura.literatura.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class ConverterDados implements IConverteDados {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o json para a classe " + classe.getSimpleName(), e);
        }
    }

    private static final Map<String, String> abreviacaoParaNome = new HashMap<>();
    private static final Map<String, String> nomeParaAbreviacao = new HashMap<>();

    static {
        abreviacaoParaNome.put("pt", "Português");
        abreviacaoParaNome.put("en", "Inglês");
        abreviacaoParaNome.put("fr", "Francês");
        abreviacaoParaNome.put("es", "Espanhol");

        for (Map.Entry<String, String> entry : abreviacaoParaNome.entrySet()) {
            nomeParaAbreviacao.put(entry.getValue().toLowerCase(), entry.getKey());
        }
    }

    public static String converterAbreviacao(String abreviacao) {
        return abreviacaoParaNome.getOrDefault(abreviacao.toLowerCase(), abreviacao);
    }

    public static String converterIdioma(String idioma) {
        return nomeParaAbreviacao.getOrDefault(idioma.toLowerCase(), idioma);
    }
}

