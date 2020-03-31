package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Armazenar {
  
    public static void gravar(Deque<Processo> lista) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Processos (*.prc)", "prc");
        JFileChooser arquivo = new JFileChooser();
        arquivo.setAcceptAllFileFilterUsed(false);
        arquivo.addChoosableFileFilter(filter);
        arquivo.setCurrentDirectory( new File(System.getProperty("user.dir")));

        try {
            if (arquivo.showOpenDialog(arquivo) == JFileChooser.APPROVE_OPTION) {
                File file = arquivo.getSelectedFile();
                if(!file.getName().endsWith("prc"))
                    file = new File(file.getPath()+".prc");
                if(file.exists()) {
                    int opc = JOptionPane.showConfirmDialog(null, "Sobrescrever o arquivo?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
                    if(opc == JOptionPane.NO_OPTION)
                        return;
                }
                FileOutputStream isTwo = new FileOutputStream(file.getPath());
                OutputStreamWriter isr = new OutputStreamWriter(isTwo);

                for (Processo p : lista) {
                    try {
                    	isr.write("Id: " + p.getId());
                        isr.write("Chegada: " + p.getChegada());
                        isr.write(";");
                        isr.write("Dura��o: " + p.getDuracao());
                        isr.write(";");
                        isr.write("Prioridade: " + p.getPrioridade());
                        isr.write("\n");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro durante a gravação\n" + ex.getMessage(), "Atenção", JOptionPane.ERROR_MESSAGE);
                    }
                }
                isr.close();
                isTwo.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro no arquivo de saída:\n" + ex.toString());
        }
    }
}