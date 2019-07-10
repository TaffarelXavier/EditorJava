package Conexao;

import java.sql.*;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Taffrel Xavier <taffarel_deus@hotmail.com>
 */
public class ConexaoBD {

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/banco_ifto";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "chkdsk");
            conn = DriverManager.getConnection(url, props);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw new SQLException();
        }
    }

    public static void fecharConexao() throws SQLException {
        ConexaoBD.conn.close();
    }
}
