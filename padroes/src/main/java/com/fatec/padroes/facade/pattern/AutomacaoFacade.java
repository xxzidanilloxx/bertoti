package com.fatec.padroes.facade.pattern;

public class AutomacaoFacade {
    private SistemaSeguranca sistemaSeguranca;
    private ControleLuzes controleLuzes;

    public AutomacaoFacade() {
        this.sistemaSeguranca = new SistemaSeguranca();
        this.controleLuzes = new ControleLuzes();
    }

    public void sairCasa() {
        sistemaSeguranca.ativarSistemaSeguranca();
        controleLuzes.apagarLuzes();
        System.out.println("Saindo de casa.");
    }

    public void voltarCasa() {
        sistemaSeguranca.desativarSistemaSeguranca();
        controleLuzes.ligarLuzes();
        System.out.println("Entrando em casa.");
    }
}
