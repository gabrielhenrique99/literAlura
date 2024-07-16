package com.alura.literatura.exception;

public class ErroDeConversaoDeAutor extends RuntimeException{
    private String mensagem;

    public ErroDeConversaoDeAutor(String mensagem){
        this.mensagem = mensagem;
    }

    public String getMensagem(){
        return mensagem;
    }
}
