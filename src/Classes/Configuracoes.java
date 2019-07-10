/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Taffarel
 */
public class Configuracoes {

    private static String caminhoJavaJDK = "";

    /**
     *
     * @return
     */
    private static String[] caminhosJDKs() {

        String driveC = File.listRoots()[0].getAbsoluteFile().toString();

        File files = new File(driveC);

        if (files.exists()) {

            String[] directories = files.list((File current, String name) -> new File(current, name).isDirectory());

            String dir = driveC + "" + directories[1] + "\\java";

            String[] jdkDir = new String[dirCountDirs(dir)];

            File caminhoExiste = new File(dir);
            //Verifica se o caminho existe
            if (caminhoExiste.exists()) {
                String[] caminhos = caminhoExiste.list((File current, String name) -> new File(current, name).isDirectory());
                jdkDir = caminhos;
            }

            Pattern regex = Pattern.compile("((jdk).*)");

            String[] nomesJDK = new String[5];

            int i = -1;

            for (String arq : jdkDir) {

                Matcher matches = regex.matcher(arq);

                if (matches.find()) {
                    i++;
                    nomesJDK[i] = matches.group();
                }
            }
            return nomesJDK;
        }
        return null;
    }

    /**
     *
     * @param caminho
     * @return
     */
    public static int dirCountDirs(String caminho) {
        File file = new File(caminho);
        File[] files = file.listFiles((File f) -> f.isDirectory());
        return files.length;
    }

    public static void main(String[] args) {
        caminhosJDKs();
    }

    public static String getCaminhoJavaJDK() {
        return caminhoJavaJDK;
    }

    public static void setCaminhoJavaJDK(String caminhoJavaJDK) {
        Configuracoes.caminhoJavaJDK = caminhoJavaJDK;
    }

    /**
     * <p style="font:16px arial">Aliás da função caminhosJDKs();</p>
     *
     * @return
     */
    public static String[] mostrarPastasJDKInstaladas() {
        return caminhosJDKs();
    }

    public static void setCaminhoJava() {

        String driveC = File.listRoots()[0].getAbsoluteFile().toString();

        File files = new File(driveC);

        String[] directories = files.list((File current, String name) -> {
            Pattern p = Pattern.compile("(Arquivos|Arquivo)");

            Matcher m = p.matcher(name);

            if (m.find()) {
                setCaminhoJavaJDK(driveC + name + "\\java");
            }
            return new File(current, name).isDirectory();
        });
    }

}
