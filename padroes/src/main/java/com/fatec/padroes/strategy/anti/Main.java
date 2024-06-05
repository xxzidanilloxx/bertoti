package com.fatec.padroes.strategy.anti;

public class Main {
    public static void main(String[] args) {
        Calculadora calculadora = new Adicao();
        System.out.println("Adição: " + calculadora.calcular(2, 1));

        calculadora = new Subtracao();
        System.out.println("Subtração: " + calculadora.calcular(2, 1));
    }
}
