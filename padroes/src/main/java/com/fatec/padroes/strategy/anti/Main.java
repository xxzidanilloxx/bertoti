package com.fatec.padroes.strategy.anti;

public class Main {
    public static void main(String[] args) {
        Calculadora calculadora = new Calculadora("add");
        System.out.println("Adição " + calculadora.executar(2, 1));

        calculadora = new Calculadora("subtract");
        System.out.println("Subtração: " + calculadora.executar(2, 1));
    }
}
