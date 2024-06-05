package com.fatec.padroes.observer.anti;

public class Alarme {
    private String local;

    public Alarme(String local) {
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void alertar(String alerta) {
        System.out.println("Local: " + local + " Alerta: " + alerta);
    }
}
