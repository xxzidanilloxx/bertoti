package com.fatec.padroes.composite.anti;

public class Main {
    public static void main(String[] args) {
        Gerente gerente = new Gerente("Antonio", "Gerente");
        Vendedor vendedor1 = new Vendedor("Carlos", "Vendedor");
        Vendedor vendedor2 = new Vendedor("Igor", "Vendedor");
        Vendedor vendedor3 = new Vendedor("Yuri", "Vendedor");

        gerente.adicionarFuncionario(vendedor1);
        gerente.adicionarFuncionario(vendedor2);

        Empresa empresa = new Empresa();
        empresa.adicionarFuncionario(gerente);
        empresa.adicionarFuncionario(vendedor3);

        empresa.mostrarDetalhesFuncionario();
    }
}
