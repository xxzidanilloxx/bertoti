package com.fatec.padroes.composite.pattern;

import java.util.List;
import java.util.ArrayList;

public class Empresa implements Funcionario{
    private List<Funcionario> funcionarios = new ArrayList<>();

    public void adicionarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    @Override
    public void mostrarDetalhesFuncionario() {
        for (Funcionario funcionario : funcionarios) {
            funcionario.mostrarDetalhesFuncionario();
        }
    }
}
