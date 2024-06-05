package com.fatec.padroes.observer.pattern;

public class Main {
    public static void main(String[] args) {
        SistemaSeguranca sistemaSeguranca = new SistemaSeguranca();

        Alarme local1 = new Alarme("Cozinha");
        Alarme local2 = new Alarme("Área de serviço.");

        sistemaSeguranca.registerObserver(local1);
        sistemaSeguranca.registerObserver(local2);

        sistemaSeguranca.setAlerta("Atividade suspeita.");
        sistemaSeguranca.setAlerta("Fogo!");
    }
}
