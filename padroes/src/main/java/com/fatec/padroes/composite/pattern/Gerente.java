package com.fatec.padroes.composite.pattern;

public class Gerente implements Funcionario{
    private String nome;
    private String cargo;

    public Gerente(String nome, String cargo) {
        this.nome = nome;
        this.cargo = cargo;
    }

    @Override
    public void mostrarDetalhesFuncionario() {
        System.out.println(nome + " - " + cargo);
    }
}
