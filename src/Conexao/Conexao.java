/**
 * Esta classe foi criada em 28/02/2018
 * pelo Grupo 5 - LC IFTO
 */
package Conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Faz as conexões com o banco de dados.
 *
 * @author Taffarel Xavier
 */
public class Conexao {

    /**
     * O Usuário do banco
     */
    private static final String USUARIO = "root";
    /**
     * A senha do banco
     */
    private static final String SENHA = "chkdsk";
    /**
     * bancoifto_db=O Banco de dados
     */
    private static final String URL = "jdbc:mysql://localhost/editor";
    /**
     * O Drive do Mysql
     */
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    public Conexao() {
    } //Possibilita instancias

    /**
     * <h2>Abre a conexão com o banco.</h2>
     * @return 
     */
    public static Connection abrir() throws ClassNotFoundException {
        try {
            // Registrar o driver
                Class.forName(DRIVER);
                // Capturar a conexão
                Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
                // Retorna a conexao aberta
                return conn;
        } catch (SQLException e) {
            switch (e.getSQLState()) {
                case "28000":
                    JOptionPane.showMessageDialog(null, "A senha do banco está incorreta.\n" + e.getMessage(), "Atenção", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                case "08S01":
                    JOptionPane.showMessageDialog(null, "O servidor de banco de dados SQL não está ligado.\n" + e.getMessage().replaceAll("\\.", "\n"),
                            "Atenção", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(null, "Houve um erro.\n" + e.getMessage(), "Atenção", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
        return null;
    }

    /**
     * Fecha a Conexão
     *
     * @throws Exception
     */
    public static void fecharConexao() throws Exception {
        abrir().close();
    }
}
