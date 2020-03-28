package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.Escalonador;
import application.Processo;

public class Armazenar {
	 /**
     * Carrega os dados do arquivo de entrada em um vetor de processos.
     * É feita a validação no formato do aquivo e dados dos processos.
     * @return lista de processos carregados (null em caso de erro)
     */
    public static ArrayList<Processo> ler() {
        ArrayList<Processo> lista_procs = null;
        String linha;
        int i;
        String pNome;
        String sCheg, sDur, sPrio;
        int iCheg, iDur, iPrio;

        try {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Processos (*.prc)", "prc");
            javax.swing.JFileChooser arquivo = new javax.swing.JFileChooser();
            arquivo.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
            arquivo.addChoosableFileFilter(filter);
            arquivo.setCurrentDirectory( new java.io.File(System.getProperty("user.dir")));

            if (arquivo.showOpenDialog(arquivo) == javax.swing.JFileChooser.APPROVE_OPTION) {
                File file = arquivo.getSelectedFile();

                java.io.FileInputStream isTwo = new java.io.FileInputStream("" + file.getPath());
                InputStreamReader isr = new InputStreamReader(isTwo);

                BufferedReader br = new BufferedReader(isr);

                lista_procs = new ArrayList<>();

                i = 1;
                while ((linha = br.readLine()) != null) {
                    
                    if (!linha.isEmpty()) //ignora linhas em branco no arquivo
                    {
                        pNome = "P" + i;
                        StringTokenizer st = new StringTokenizer(linha, ";");
                        try {
                            sCheg = st.nextToken();
                            sDur = st.nextToken();
                            sPrio = st.nextToken();

                            iCheg = Integer.parseInt(sCheg);
                            iDur = Integer.parseInt(sDur);
                            iPrio = Integer.parseInt(sPrio);
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

                if (!Escalonador.validaEntrada(lista_procs)) {
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
    public static void gravar(ArrayList<Processo> lista) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Processos (*.prc)", "prc");
        javax.swing.JFileChooser arquivo = new javax.swing.JFileChooser();
        arquivo.setAcceptAllFileFilterUsed(false);
        arquivo.addChoosableFileFilter(filter);
        arquivo.setCurrentDirectory( new java.io.File(System.getProperty("user.dir")));

        try {
            if (arquivo.showOpenDialog(arquivo) == javax.swing.JFileChooser.APPROVE_OPTION) {
                File file = arquivo.getSelectedFile();
                if(!file.getName().endsWith("prc"))
                    file = new File(file.getPath()+".prc");
                if(file.exists())
                {
                    int opc = JOptionPane.showConfirmDialog(null, "Sobrescrever o arquivo?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
                    if(opc == JOptionPane.NO_OPTION)
                        return;

                }
                java.io.FileOutputStream isTwo = new java.io.FileOutputStream("" + file.getPath());
                OutputStreamWriter isr = new OutputStreamWriter(isTwo);

                for (Processo p : lista) {
                    try {
                        isr.write("" + p.getChegada());
                        isr.write(";");
                        isr.write("" + p.getDuracao());
                        isr.write(";");
                        isr.write("" + p.getPrioridade());
                        isr.write("\n");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro durante a gravação\n" + ex.getMessage(), "Atenção", JOptionPane.ERROR_MESSAGE);
                    }

                }
                isr.close();
                isTwo.close();
            }
        } catch (java.io.IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Erro no arquivo de saída:\n" + ex.toString());
        }

    }
}
