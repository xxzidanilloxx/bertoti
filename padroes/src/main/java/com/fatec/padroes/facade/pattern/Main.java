package com.fatec.padroes.facade.pattern;

public class Main {
    public static void main(String[] args) {
        AutomacaoFacade automacaoFacade = new AutomacaoFacade();
        automacaoFacade.sairCasa();
        automacaoFacade.voltarCasa();
    }
}
