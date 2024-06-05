package com.fatec.padroes.strategy.anti;

public class Subtracao extends Calculadora {
    @Override
    public int calcular(int a, int b) {
        return a - b;
    }
}
