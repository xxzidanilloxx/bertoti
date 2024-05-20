package com.fatec.padroes.strategy.pattern;

public class SubtracaoStrategy implements Strategy{
    public int executar(int a, int b) {
        return a - b;
    }
}
