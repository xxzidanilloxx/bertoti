package com.fatec.padroes.composite.anti;

public class Vendedor {
    private String nome;
    private String cargo;

    public Vendedor(String nome, String cargo) {
        this.nome = nome;
        this.cargo = cargo;
    }

    public void mostrarDetalhesFuncionario() {
        System.out.println(nome + " - " + cargo);
    }

    public void adicionarFuncionario(Object funcionario) {
        throw new UnsupportedOperationException("Vendedores não podem adicionar funcionários.");
    }
}
