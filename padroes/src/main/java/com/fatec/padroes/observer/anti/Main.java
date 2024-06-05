package com.fatec.padroes.observer.anti;

public class Main {
    public static void main(String[] args) {
        SistemaSeguranca sistemaSeguranca = new SistemaSeguranca();

        Alarme local1 = new Alarme("Cozinha");
        Alarme local2 = new Alarme("Área de serviço");

        sistemaSeguranca.alertarTodos("Atividade suspeita.");
        local1.alertar(sistemaSeguranca.getAlerta());
        local2.alertar(sistemaSeguranca.getAlerta());

        sistemaSeguranca.alertarTodos("Fogo!");
        local1.alertar(sistemaSeguranca.getAlerta());
        local2.alertar(sistemaSeguranca.getAlerta());
    }
}
