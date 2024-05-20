package com.fatec.padroes.strategy.pattern;

public class Calculadora {
    private Strategy strategy;

    public Calculadora(Strategy strategy) {
        this.strategy = strategy;
    }

    public int executeStrategy(int a, int b) {
        return strategy.executar(a, b);
    }
}
