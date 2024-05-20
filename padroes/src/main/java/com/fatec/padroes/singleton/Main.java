package com.fatec.padroes.singleton;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.INSTANCE.getConnection();

        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Sucesso!");
            } else {
                System.out.println("Erro.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
