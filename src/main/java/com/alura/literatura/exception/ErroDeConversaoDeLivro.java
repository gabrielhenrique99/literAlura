package com.alura.literatura.exception;

public class ErroDeConversaoDeLivro extends RuntimeException{

    private String mensagem;

    public ErroDeConversaoDeLivro(String mensagem){
        this.mensagem = mensagem;
    }

    public String getMensagem(){
        return mensagem;
    }
}
