package com.fatec.padroes.strategy.pattern;

public class AdicaoStrategy implements Strategy {
    public int executar(int a, int b) {
        return a + b;
    }
}
