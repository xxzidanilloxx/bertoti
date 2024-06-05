package com.fatec.padroes.composite.anti;

import java.util.ArrayList;
import java.util.List;

public class Gerente {
    private String nome;
    private String cargo;
    private List<Object> subordinados;

    public Gerente(String nome, String cargo) {
        this.nome = nome;
        this.cargo = cargo;
        this.subordinados = new ArrayList<>();
    }

    public void mostrarDetalhesFuncionario() {
        System.out.println(nome + " - " + cargo);
        for (Object subordinado : subordinados) {
            if (subordinado instanceof Gerente) {
                ((Gerente) subordinado).mostrarDetalhesFuncionario();
            } else if (subordinado instanceof Vendedor) {
                ((Vendedor) subordinado).mostrarDetalhesFuncionario();
            }
        }
    }

    public void adicionarFuncionario(Object funcionario) {
        subordinados.add(funcionario);
    }
}
