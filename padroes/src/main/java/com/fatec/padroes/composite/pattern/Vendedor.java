package com.fatec.padroes.composite.pattern;

public class Vendedor implements Funcionario{
    private String nome;
    private String cargo;

    public Vendedor(String nome, String cargo) {
        this.nome = nome;
        this.cargo = cargo;
    }

    @Override
    public void mostrarDetalhesFuncionario() {
        System.out.println(nome + " - " + cargo);
    }
}
