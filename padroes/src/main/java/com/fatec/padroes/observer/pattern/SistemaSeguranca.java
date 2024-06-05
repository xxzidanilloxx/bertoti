package com.fatec.padroes.observer.pattern;

import java.util.ArrayList;
import java.util.List;

public class SistemaSeguranca implements Subject {
    private List<Observer> observers;
    private String alerta;

    public SistemaSeguranca() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(alerta);
        }
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
        notifyObservers();
    }
}
