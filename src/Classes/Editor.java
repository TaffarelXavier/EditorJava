package Classes;

import Telas.TelaConfiguracoes;
import Telas.TelaEditor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import tx.utilitarios.*;

/**
 *
 * @author Taffarel
 */
public class Editor {

    //private String defaultDirectory = null;
    private static String getDesktop = null;

    public static String caminhoArquivosDeProgramas = null;

    /**
     *
     * @param novoJFrame
     * @param textArea
     * @param nomeArquivoJava
     * @param classePrincipal
     * @param nomeDoDiretorio
     * @param jdkPathDir
     */
    public static void criarMenu(JFrame novoJFrame,
            JTextArea textArea,
            JTextField nomeArquivoJava,
            JTextField classePrincipal,
            JTextField nomeDoDiretorio,
            String jdkPathDir
    ) {

        //Primeiro passo: criar um objeto JMenuBar
        JMenuBar menuBar = new JMenuBar();

        //Segundo passo: criar um objeto JMenu
        JMenu menu = new JMenu("Arquivo");
        JMenu executar = new JMenu("Executar");

        //Segundo passo: criar o itens com o  objeto JMenuItem
        JMenuItem fechar = new JMenuItem("Fechar");
        JMenuItem abrir = new JMenuItem("Abrir");
        JMenuItem salvar = new JMenuItem("Salvar");
        JMenuItem configuracoes = new JMenuItem("Configurações");
        JMenuItem novo = new JMenuItem("Novo");
        JMenuItem sair = new JMenuItem("Sair");

        JMenuItem salvarECompilar = new JMenuItem("Salvar e Compilar");

        //Quarto passo: adicionar os MenuItens nos menus
        menu.add(novo);
        menu.add(abrir);
        menu.add(salvar);
        menu.add(configuracoes);
        menu.addSeparator();
        menu.add(fechar);
        menu.addSeparator();
        menu.add(sair);

        executar.add(salvarECompilar);

        //Quinto passo: adicionar os JMenu no menuBar
        menuBar.add(menu);
        menuBar.add(executar);

        //Sexo passo: adicionar o JMenuBar no JFrame no método setJMenuBar
        novoJFrame.setJMenuBar(menuBar);
        /**
         * Abrir Arquivo
         */
        abrir.addActionListener((ActionEvent e) -> {
            abrirArquivo(textArea, nomeArquivoJava, classePrincipal, nomeDoDiretorio);
        });
        /**
         * <p style="font:16px arial">Salvar</p>
         */
        salvar.addActionListener((ActionEvent e) -> {
            salvarArquivo(
                    nomeArquivoJava,
                    textArea.getText(),
                    classePrincipal.getText(),
                    nomeArquivoJava,
                    nomeDoDiretorio,
                    jdkPathDir);
        });
        /**
         * Limpar Campos
         */
        fechar.addActionListener((ActionEvent e) -> {
            textArea.setText("");
            nomeArquivoJava.setText("");
            classePrincipal.setText("");
            nomeDoDiretorio.setText("");
        });

        salvarECompilar.addActionListener((ActionEvent e) -> {

            if (isMainExiste(textArea.getText())) {
                File fJava = new File(nomeArquivoJava.getText().trim());

                if (!fJava.isFile()) {
                    JOptionPane.showMessageDialog(null, "O arquivo java não é válido.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                } else if ("".equals(nomeArquivoJava.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Há elementos faltando na IDE.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    salvarECompilarArquivo(nomeArquivoJava.getText(), textArea.getText(), nomeDoDiretorio.getText(), classePrincipal.getText());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Faltando a classe principal.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            }

        });

        configuracoes.addActionListener((ActionEvent e) -> {

            if (!new TelaConfiguracoes().isVisible()) {
                new TelaConfiguracoes().setVisible(true);
            }

        });

        novo.addActionListener((e) -> {
            TelaEditor telaEditor = new TelaEditor();
            telaEditor.setVisible(true);
        });

        sair.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
    }

    /**
     *
     * @param texto
     * @return
     */
    public static boolean isMainExiste(String texto) {
        // String to be scanned to find the pattern.
        String pattern = "(.*)(public\\s+static\\s+void\\s+main\\(String\\[\\]\\s+\\w+\\))(.*)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(texto);

        return m.find();
    }

    /**
     *
     * @param textArea
     * @param nomeArquivoJava
     * @param classePrincipal
     * @param nomeDoDiretorio
     */
    public static void abrirArquivo(
            JTextArea textArea,
            JTextField nomeArquivoJava,
            JTextField classePrincipal,
            JTextField nomeDoDiretorio) {

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setPreferredSize(new Dimension(900, 590));
        try {
            //defaultDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getCanonicalPath();
            getDesktop = FileSystemView.getFileSystemView().getHomeDirectory().getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }

        File currentDir = new File(getDesktop + "\\java");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo java file(.java)", "java");

        jFileChooser.setFileFilter(filter);

        if (!TXProperties.keyExists("ultimo.caminho.programa")) {
            currentDir = new File(getDesktop + "\\java");
        } else {
            currentDir = new File(TXProperties.getProp("ultimo.caminho.programa"));
        }

        jFileChooser.setCurrentDirectory(currentDir);

        int result = jFileChooser.showOpenDialog(null);

        File currentDirectory = jFileChooser.getCurrentDirectory();

        StringBuffer buffer;

        buffer = new StringBuffer();

        if (result == JFileChooser.APPROVE_OPTION) {

            FileReader reader;

            reader = null;

            File file = jFileChooser.getSelectedFile();
            try {
                reader = new FileReader(file);
                int i = 1;
                while (i != -1) {
                    try {
                        i = reader.read();
                    } catch (IOException ex) {
                        Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    char ch = (char) i;
                    buffer.append(ch);

                }
                nomeDoDiretorio.setText(currentDirectory.getAbsolutePath());

                nomeArquivoJava.setText(jFileChooser.getSelectedFile().getCanonicalPath());

                textArea.setText(buffer.toString().trim().replaceAll("￿", ""));
                //Pega o nome da Classe
                String pattern = "(class).*?(\\w+)";

                // Create a Pattern object
                Pattern r = Pattern.compile(pattern);

                // Now create matcher object.
                Matcher m = r.matcher(buffer.toString());
                if (m.find()) {
                    classePrincipal.setText(m.group(2));
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *
     * @param textArea
     * @param classePrincipal
     * @param jFielfNomeArquivo
     * @param jFieldDir
     */
    public static void limparCampos(
            JTextArea textArea,
            JTextField classePrincipal,
            JTextField jFielfNomeArquivo,
            JTextField jFieldDir) {
        textArea.setText("");
        textArea.requestFocus();
        classePrincipal.setText("");
        jFielfNomeArquivo.setText("");
        jFieldDir.setText("");
    }

    /**
     *
     * @param arquivoNome
     * @param content
     * @param classePrincipal
     * @param nomeDoArquivoJava
     * @param jFieldDir
     * @param jdkPath
     */
    public static void salvarArquivo(
            JTextField arquivoNome,
            String content,
            String classePrincipal,
            JTextField nomeDoArquivoJava,
            JTextField jFieldDir,
            String jdkPath) {

        try {
            //defaultDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getCanonicalPath();
            getDesktop = FileSystemView.getFileSystemView()
                    .getHomeDirectory().getCanonicalPath();

        } catch (IOException ex) {
            Logger.getLogger(Editor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        if (classePrincipal.equals("")) {
            JOptionPane.showMessageDialog(null, "Digite o nome da Classe Principal", "Atenção",
                    JOptionPane.ERROR_MESSAGE);
        } else if (arquivoNome.getText().equals("")) {

            JFileChooser jFileChooser = new JFileChooser();

            jFileChooser.setAcceptAllFileFilterUsed(false);

            jFileChooser.setPreferredSize(new Dimension(900, 590));

            jFileChooser.setDialogTitle("Escolha um nome para salvar");

            File currentDir;
            //Se a chave não existe, pega o caminho do java.
            if (!TXProperties.keyExists("ultimo.caminho.programa")) {
                currentDir = new File(getDesktop + "\\java");
            } else {
                currentDir = new File(TXProperties.getProp("ultimo.caminho.programa"));
            }

            jFileChooser.setCurrentDirectory(currentDir);

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo java file(.java)", "java");

            jFileChooser.setFileFilter(filter);
            //O Nome do Arquivo
            //String FileName = jTextField2.getText() + ".java";
            String FileName = classePrincipal + ".java";

            File fileNameJava = new File(FileName);

            jFileChooser.setSelectedFile(fileNameJava);

            int ret = jFileChooser.showSaveDialog(null);

            File arquivoJava = jFileChooser.getSelectedFile();
            //Verifica se o arquivo existe
            if (arquivoJava.exists()) {

                int reply = JOptionPane.showConfirmDialog(
                        null,
                        "O arquivo \"" + arquivoJava.getName() + "\" já existe, deseja sobrescrever?",
                        "Atenção",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        try {

                            //Pega o nome do arquivo .Java
                            //jField3
                            nomeDoArquivoJava.setText(arquivoJava.getCanonicalPath());

                            File currentDirectory = jFileChooser.getCurrentDirectory();

                            //jField1
                            jFieldDir.setText(currentDirectory.getAbsolutePath());

                            try (PrintWriter writer = new PrintWriter(currentDirectory + "\\" + FileName, "UTF-8")) {
                                String[] words = content.split("\\n");
                                for (String word : words) {
                                    writer.println(word);
                                }
                            }

                            File caminhoCompilador = new File(currentDirectory + "\\compilador.bat");

                            criarDir(currentDirectory);

                            TXProperties.addItem("ultimo.caminho.programa", currentDirectory.getAbsolutePath());

                            try (PrintWriter writer2 = new PrintWriter(caminhoCompilador, "UTF-8")) {
                                String cmdsContent = "@echo off\n cd " + jdkPath + "\\bin\n javac -d "
                                        + currentDirectory + "\\bin " + currentDirectory + "\\" + classePrincipal
                                        + ".java\njava -cp " + currentDirectory + "\\bin " + classePrincipal + "\npause";

                                String[] cmdArray = cmdsContent.split("\\n");

                                for (String cmd : cmdArray) {
                                    writer2.println(cmd);
                                }
                            }

                        } catch (IOException e) {
                        }
                    }
                }
            } else {
                if (ret == JFileChooser.APPROVE_OPTION) {
                    try {

                        //Pega o nome do arquivo .Java
                        //jField3
                        nomeDoArquivoJava.setText(arquivoJava.getCanonicalPath());

                        File currentDirectory = jFileChooser.getCurrentDirectory();

                        //jField1
                        jFieldDir.setText(currentDirectory.getAbsolutePath());

                        try (PrintWriter writer = new PrintWriter(currentDirectory + "\\" + FileName, "UTF-8")) {
                            String[] words = content.split("\\n");
                            for (String word : words) {
                                writer.println(word);
                            }
                        }

                        File caminhoCompilador = new File(currentDirectory + "\\compilador.bat");

                        criarDir(currentDirectory);
                        
                        TXProperties.addItem("ultimo.caminho.programa", currentDirectory.getAbsolutePath());

                        try (PrintWriter writer2 = new PrintWriter(caminhoCompilador, "UTF-8")) {
                            String cmdsContent = "@echo off\n cd " + jdkPath + "\\bin\n javac -d "
                                    + currentDirectory + "\\bin " + currentDirectory + "\\" + classePrincipal
                                    + ".java\njava -cp " + currentDirectory + "\\bin " + classePrincipal + "\npause";

                            String[] cmdArray = cmdsContent.split("\\n");

                            for (String cmd : cmdArray) {
                                writer2.println(cmd);
                            }
                        }

                    } catch (IOException e) {
                    }
                }
            }
        } else {
            //Se todos os campos estiverem preenchidos
            try (PrintWriter writer = new PrintWriter(nomeDoArquivoJava.getText(), "UTF-8")) {
                String[] words = content.split("\\n");
                for (String word : words) {
                    writer.println(word);
                }
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(TelaEditor.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * <p style="font-weight:900;">Criar diretório</p>
     * @param currentDirectory 
     */
    public static void criarDir(File currentDirectory) {
        File theDir = new File(currentDirectory + "\\bin");
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("Criano novo diretório: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
    }

    /**
     *
     * @param arquivoJava
     * @param content
     * @param dir
     * @param classePrincipalNome
     */
    public static void salvarECompilarArquivo(
            String arquivoJava,
            String content,
            String dir,
            String classePrincipalNome) {

        if (!arquivoJava.equals("") && !content.equals("")) {

            try (PrintWriter writer = new PrintWriter(arquivoJava, "UTF-8")) {

                String[] words = content.split("\\n");

                for (String word : words) {
                    writer.println(word + "\\n");
                }

            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Atenção 2", JOptionPane.ERROR_MESSAGE);
            } catch (UnsupportedEncodingException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Atenção 3", JOptionPane.ERROR_MESSAGE);
            }

            Runtime exeCmd = Runtime.getRuntime();

            try {
                File caminhoCompilador = new File(dir + "\\compilador.bat");

                PrintWriter writer = new PrintWriter(caminhoCompilador, "UTF-8");

                String cmdsContent = "@echo off\ncd C:\\Program Files\\Java\\jdk1.8.0_144\\bin\njavac -d "
                        + dir + "\\bin " + dir + "\\" + classePrincipalNome
                        + ".java\njava -cp " + dir + "\\bin " + classePrincipalNome;

                String[] cmdArray = cmdsContent.split("\\n");

                BufferedWriter bw = new BufferedWriter(writer);

                for (String a : cmdArray) {
                    bw.append(a);
                }

                bw.close();
                writer.close();

                exeCmd.exec("cmd /c cd \"" + dir + "\" & start cmd.exe /k " + dir + "\\compilador.bat");

            } catch (FileNotFoundException ex) {
                Logger.getLogger(TelaEditor.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(TelaEditor.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(TelaEditor.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * <p>
     * Faz a compilação</p>
     *
     * @param dir O Diretório onde o arquivo java foi salvo
     */
    public static void compilar(String dir) {

        Runtime rt = Runtime.getRuntime();

        try {
            rt.exec("cmd /c cd \"" + dir + "\" & start cmd.exe /k " + dir + "\\compilador.bat");

        } catch (IOException ex) {
            Logger.getLogger(TelaEditor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param evt
     * @param arquivoNome
     * @param content
     * @param classePrincipal
     * @param jFielfNomeArquivo
     * @param jFieldDir
     * @param jdkPathDir
     * @param buttonSalvar
     */
    public static void executarKeyComando(
            java.awt.event.KeyEvent evt,
            JTextField arquivoNome,
            String content,
            String classePrincipal,
            JTextField jFielfNomeArquivo,
            JTextField jFieldDir,
            String jdkPathDir,
            JButton buttonSalvar) {

        //System.out.println("evt.getKeyCode=" + evt.getKeyCode());
//F5 - compilar
        if (evt.getKeyCode() == 116) {
            compilar(jFieldDir.getText());
            //Editor.salvarArquivo(arquivoNome, jTextArea1.getText(), jTextField2.getText(), jTextField3, jTextField1);
            buttonSalvar.setEnabled(false);
        }

        if (!(evt.isControlDown() && evt.getKeyCode() == 83)) {
            buttonSalvar.setEnabled(true);
        }

        if (evt.isControlDown() && evt.getKeyCode() == 83) {
            buttonSalvar.setEnabled(false);
            Editor.salvarArquivo(arquivoNome, content, classePrincipal, jFielfNomeArquivo, jFieldDir, jdkPathDir);

        }
        //F6 salvar e compilar
        if (evt.getKeyCode() == 117) {
            buttonSalvar.setEnabled(false);
            Editor.salvarECompilarArquivo(arquivoNome.getText(), content, jFieldDir.getText(), classePrincipal);

        }
    }
}
