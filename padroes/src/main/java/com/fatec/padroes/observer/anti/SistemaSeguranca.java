package com.fatec.padroes.observer.anti;

public class SistemaSeguranca {
    private String alerta;

    public SistemaSeguranca() {
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public void alertarTodos(String alerta) {
        this.alerta = alerta;
    }
}
