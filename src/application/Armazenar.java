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
	 /**
     * Carrega os dados do arquivo de entrada em um vetor de processos.
     * É feita a validação no formato do aquivo e dados dos processos.
     * @return lista de processos carregados (null em caso de erro)
     */
    public static Deque<Processo> ler() {
        Deque<Processo> lista_procs = null;
        String linha;
        Long i;
        Long pNome;
        String sCheg, sDur, sPrio;
        Long iCheg, iDur, iPrio;

        try {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Processos (*.prc)", "prc");
            JFileChooser arquivo = new JFileChooser();
            arquivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
            arquivo.addChoosableFileFilter(filter);
            arquivo.setCurrentDirectory( new File(System.getProperty("user.dir")));

            if (arquivo.showOpenDialog(arquivo) == JFileChooser.APPROVE_OPTION) {
                File file = arquivo.getSelectedFile();

                java.io.FileInputStream isTwo = new FileInputStream("" + file.getPath());
                InputStreamReader isr = new InputStreamReader(isTwo);

                BufferedReader br = new BufferedReader(isr);

                lista_procs = new ArrayDeque<>();

                i = 1L;
                while ((linha = br.readLine()) != null) {
                    
                    if (!linha.isEmpty()) //ignora linhas em branco no arquivo
                    {
                        pNome = i;
                        StringTokenizer st = new StringTokenizer(linha, ";");
                        try {
                            sCheg = st.nextToken();
                            sDur = st.nextToken();
                            sPrio = st.nextToken();

                            iCheg = Long.parseLong(sCheg);
                            iDur = Long.parseLong(sDur);
                            iPrio = Long.parseLong(sPrio);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Arquivo de entrada com formato Inválido!", "Atenção", JOptionPane.ERROR_MESSAGE);
                            isr.close();
                            isTwo.close();
                            lista_procs = null;
                            return null;
                        }

                        lista_procs.add(new Processo(pNome, iCheg, iDur, iPrio));

                        i++;
                    }
                }
                isr.close();
                isTwo.close();

                if (!Escalonador.validateEntry(lista_procs)) {
                    JOptionPane.showMessageDialog(null, "Arquivo de entrada com valores Inválidos!", "Atenção", JOptionPane.ERROR_MESSAGE);
                    lista_procs = null;
                    return null;
                }

            }
        } catch (java.io.IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Erro no arquivo de entrada: " + ex.toString());
        }

        return lista_procs;
    }

    /**
     * Grava em arquivo os dados de uma lista de processos
     * @param lista processos a serem gravados
     */    
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
                if(file.exists())
                {
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
