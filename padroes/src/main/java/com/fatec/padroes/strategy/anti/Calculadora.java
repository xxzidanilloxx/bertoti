package com.fatec.padroes.strategy.anti;

public class Calculadora {
    private String operacao;

    public Calculadora(String operacao) {
        this.operacao = operacao;
    }

    public int executar(int a, int b) {
        if (operacao.equals("add")) {
            return a + b;
        } else {
            return a - b;
        }
    }
}
