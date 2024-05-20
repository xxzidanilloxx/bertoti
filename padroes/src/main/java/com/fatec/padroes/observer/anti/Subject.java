package com.fatec.padroes.observer.anti;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<Observer> observers = new ArrayList<>();
    private int data;

    void addObserver(Observer observer) {
        observers.add(observer);
    }

    void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(data);
        }
    }

    void setData(int data) {
        this.data = data;
        notifyObservers();
    }
}
