package com.fatec.padroes.observer.pattern;

public interface Subject {
    void registerObserver(Observer observer);
    void notifyObservers();
}
