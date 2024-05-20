package com.fatec.padroes.observer.pattern;

public class ConcreteObserver implements Observer{
    private int data;

    @Override
    public void update(int data) {
        this.data = data;
        System.out.println("Novos dados recebidos: " + data);
    }
}
