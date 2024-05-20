package com.fatec.padroes.strategy.pattern;

public class Main {
    public static void main(String[] args) {
        Calculadora calculadora = new Calculadora(new AdicaoStrategy());
        System.out.println("Adição: " + calculadora.executeStrategy(2, 1));

        calculadora = new Calculadora(new SubtracaoStrategy());
        System.out.println("Subtração: " + calculadora.executeStrategy(2, 1));
    }
}
