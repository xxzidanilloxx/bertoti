package com.fatec.padroes.composite.anti;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private List<Object> funcionarios = new ArrayList<>();

    public void adicionarFuncionario(Object funcionario) {
        funcionarios.add(funcionario);
    }

    public void mostrarDetalhesFuncionario() {
        for (Object funcionario : funcionarios) {
            if (funcionario instanceof Gerente) {
                ((Gerente) funcionario).mostrarDetalhesFuncionario();
            } else if (funcionario instanceof Vendedor) {
                ((Vendedor) funcionario).mostrarDetalhesFuncionario();
            }
        }
    }
}
