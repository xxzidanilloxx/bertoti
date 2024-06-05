package com.fatec.padroes.observer.pattern;

public class Alarme implements Observer {
    private String local;

    public Alarme(String local) {
        this.local = local;
    }

    @Override
    public void update(String alerta) {
        System.out.println("Local: " + local + " Alerta: " + alerta);
    }
}
