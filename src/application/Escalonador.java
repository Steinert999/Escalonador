package application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class Escalonador {
	
	private List<Processo> entrada;
	private List<Processo> resultado;
	private List<Processo> pronto;
	
	private float mEspera, mTurnAround;
	
	public Escalonador(List<Processo> list) {
		setEntrada(list);
	}
	
	public static boolean validaEntrada(List<Processo> lista) {
		if(lista == null || lista.isEmpty()) return false;
		if(lista.stream().anyMatch(p -> p.getChegada() < 0 || p.getDuracao() < 0 || p.getPrioridade() < 0)) return true;
		return false;
	}
	
	public void setEntrada(List<Processo> lista) {
		if(!validaEntrada(lista)) return;
		entrada = new ArrayList<>();
		lista.stream().forEach(p -> entrada.add(new Processo(p)));
	}
	
	public void ordenar(List<Processo> p , OrdenacaoProcessos ord) {
		
		switch (ord) {
		case CHEGADA:
			p =  p.stream().sorted(Comparator.comparing(Processo::getChegada)).collect(Collectors.toList());
		case DURACAO:
			p = p.stream().sorted(Comparator.comparing(Processo::getDuracao)).collect(Collectors.toList());
		case PRIORIDADE: // decrescente
			p = p.stream().sorted(Comparator.comparing(Processo::getPrioridade).reversed()).collect(Collectors.toList());
		case NOME:
			p = p.stream().sorted(Comparator.comparing(Processo::getId)).collect(Collectors.toList());
		}
	}
	
	
	  /**
     * Escalonador First Came, First Served (FCFS)
     * Faz o escalonamento n�o preemptivo, por ordem de chegada.
     */

    public void escalonarFCFS() {

    	// se n�o h� processos validos n�o escalona
        if(!validaEntrada(entrada))
            return;
        pronto = new ArrayList<>();
        resultado = new ArrayList<>();
        Processo executando;

        int tempoAtual;

        ordenar(entrada, OrdenacaoProcessos.CHEGADA);
        mEspera = 0;
        mTurnAround = 0;

        tempoAtual = entrada.get(0).getChegada();
        
        pronto.add(entrada.get(0));
        entrada.remove(0);

         //Enquanto existem processos na fila de entrada ou de Prontos
        while (!pronto.isEmpty() || !entrada.isEmpty()) {
            //se chegaram novos processos, coloca na fila de prontos
            while (!entrada.isEmpty() && entrada.get(0).getChegada() <= tempoAtual) {
                if (entrada.get(0).getChegada() <= tempoAtual) {
                    pronto.add(entrada.get(0));
                    entrada.remove(0);
                }
            }
            if (!pronto.isEmpty()) {
                ordenar(pronto, OrdenacaoProcessos.CHEGADA);
                //pega o próximo processo de menor duração na fila de prontos
                executando = pronto.get(0);
                pronto.remove(0);
                
                //calcula os tempos para o processo atual
                executando.setEspera(tempoAtual - executando.getChegada());
                //atualiza o tempo atual
                tempoAtual += executando.getDuracao();
                //atualiza o tempo de execução do processo
                executando.setTurnaround(tempoAtual - executando.getChegada());
                resultado.add(executando);

                mEspera += executando.getEspera();
                mTurnAround += executando.getTurnaround();
            }
            else
                //há um intervalode tempo "livre", onde não há processos para executar
                if(!entrada.isEmpty())
                {
                    //aponta para o próximo da fila para pegar o tempo de chegada
                    int tempo = entrada.get(0).getChegada();
                    tempoAtual = tempo;   }
                //tempoAtual++;
        }

        mEspera /= resultado.size();
        mTurnAround /= resultado.size();
    }

    /**
     * Escalonador Sortest Job First (SJF) Não-Preemptivo
     * Faz o escalonamento não preemptivo levando em consideração a chegada e tendo
     * como prioridade os processos de menor duração.
     */
    public void escalonarSJFnP() {
        int tempoAtual;
        Processo executando;

        //se não há um conjunt de processos válidos não escalona
        if(!validaEntrada(entrada))
            return;

        resultado = new ArrayList<>();
        pronto = new ArrayList<>();

        ordenar(entrada, OrdenacaoProcessos.CHEGADA);

        mEspera = 0;
        mTurnAround = 0;
        tempoAtual = entrada.get(0).getChegada();
        pronto.add(entrada.get(0));
        entrada.remove(0);

        //Enquanto existem processos na fila de entrada ou de Prontos
        while (!pronto.isEmpty() || !entrada.isEmpty())
        {
            //se chegaram novos processos, coloca na fila de prontos
            while (!entrada.isEmpty() && entrada.get(0).getChegada() <= tempoAtual)
            {
                if (entrada.get(0).getChegada() <= tempoAtual)
                {
                    pronto.add(entrada.get(0));
                    entrada.remove(0);
                }
            }
            //verifica se existem processos na fila de prontos
            if (!pronto.isEmpty())
            {
                //pega o próximo processo de menor duração na fila de prontos
                ordenar(pronto, OrdenacaoProcessos.DURACAO);
                executando = pronto.get(0);
                pronto.remove(0);
                //calcula a execução do processo atual
                executando.setEspera(tempoAtual - executando.getChegada());
                tempoAtual += executando.getDuracao();
                executando.setTurnaround(tempoAtual - executando.getChegada());

                //adiciona ao Array de saída
                resultado.add(executando);

                mEspera += executando.getEspera();
                mTurnAround += executando.getTurnaround();
            }
            else
            {
                 //há um intervalode tempo "livre", onde não há processos para executar
                if(!entrada.isEmpty())
                {
                    //aponta para o próximo da fila para pegar o tempo de chegada
                    int tempo = entrada.get(0).getChegada();
                    tempoAtual = tempo;
                }
            }
        }
        //calcula os tempos médios de espera e turnaround
        mEspera /= resultado.size();
        mTurnAround /= resultado.size();
    }

    /**
     * Escalonador Sortest Remaining Time (SRT) Preemptivo
     * Faz o escalonamento preemptivo levando em consideração a chegada e tendo
     * como prioridade os processos de menor duração.
     * É a versão preemptiva do SJF.
     */
    public void escalonarSRT() {
        int tempoAtual;
        Processo executando = null;

        //se não há um conjunto de processos válidos não escalona
        if(!validaEntrada(entrada))
            return;

        resultado = new ArrayList<>();
        pronto = new ArrayList<>();

        ordenar(entrada, OrdenacaoProcessos.CHEGADA);

        mEspera = 0;
        mTurnAround = 0;
        tempoAtual = entrada.get(0).getChegada();
        pronto.add(entrada.get(0));
        entrada.remove(0);

        //Enquanto exixtem processos na fila de entrada ou de Prontos ou o último processo não acabou
        while (!pronto.isEmpty() || !entrada.isEmpty() || executando != null)
        {
            //se chegaram novos processos, coloca na fila de prontos
            while (!entrada.isEmpty() && entrada.get(0).getChegada() <= tempoAtual)
            {
                if (entrada.get(0).getChegada() <= tempoAtual)
                {
                    pronto.add(entrada.get(0));
                    entrada.remove(0);
                    ordenar(pronto, OrdenacaoProcessos.DURACAO);
                }
            }
            //verifica se tem processo na fila de prontos OU algum processo executando
            if (!pronto.isEmpty() || executando != null)
            {
                //Verifica se há prioridade para interromper
                if (executando != null) //se tem processo executando
                {
                    //processo na fila é de menor duração? Sim = troca
                    if (!pronto.isEmpty())
                        if (executando.getDuracao() > pronto.get(0).getDuracao())
                        {
                            executando.setInter(tempoAtual);//tempo onde foi interrompido
                            pronto.add(executando);
                            executando = pronto.get(0);
                            pronto.remove(0);
                            ordenar(pronto, OrdenacaoProcessos.DURACAO);
                            //atualiza o tempo de espera do processo
                            executando.setEspera(executando.getEspera() + tempoAtual - executando.getInter());
                        }
                }
                else //pega o primeiro dos prontos
                {
                    executando = pronto.get(0);
                    pronto.remove(0);
                    //atualiza o tempo de espera do processo
                    executando.setEspera(executando.getEspera() + tempoAtual - executando.getInter());
                }
                //calcula a execução do processo atual
                tempoAtual++;

                executando.DecDuracao();

                //se terminou adiciona ao Arry de saída
                if (executando.getDuracao() == 0)
                {
                    executando.setTurnaround(tempoAtual - executando.getChegada());
                    resultado.add(executando);
                    mEspera += executando.getEspera();
                    mTurnAround += executando.getTurnaround();
                    executando = null;
                }
            }
            else
            {
                 //há um intervalode tempo "livre", onde não há processos para executar
                if(!entrada.isEmpty())
                {
                    //aponta para o próximo da fila para pegar o tempo de chegada
                    int tempo = entrada.get(0).getChegada();
                    tempoAtual = tempo;
                }
            }

        }
        //calcula os tempos médios de espera e turnaround
        mEspera /= resultado.size();
        mTurnAround /= resultado.size();
    }

    /**
     * Escalonador por Prioridade Não-Preemptivo
     * Faz o escalonamento não preemptivo levando em consideração a chegada e tendo
     * como prioridade o valor de prioridade dos processos.
     * Valoresultado maioresultado de prioridade indicam maior importância.
     */
    public void escalonarPrioridadeNP() {
        int tempoAtual;
        Processo executando;

        //se não há um conjunt de processos válidos não escalona
        if(!validaEntrada(entrada))
            return;

        resultado = new ArrayList<>();
        pronto = new ArrayList<>();

        ordenar(entrada, OrdenacaoProcessos.CHEGADA);

        mEspera = 0;
        mTurnAround = 0;
        tempoAtual = entrada.get(0).getChegada();
        pronto.add(entrada.get(0));
        entrada.remove(0);

        //Enquanto existem processos na fila de entrada ou de Prontos
        while (!pronto.isEmpty() || !entrada.isEmpty())
        {
            //se chegaram novos processos, coloca na fila de prontos
            while (!entrada.isEmpty() && entrada.get(0).getChegada() <= tempoAtual)
            {
                if (entrada.get(0).getChegada() <= tempoAtual)
                {
                    pronto.add(entrada.get(0));
                    entrada.remove(0);
                }
            }
            if (!pronto.isEmpty())
            {
                //pega o próximo processo de maior prioridade na fila de prontos
                ordenar(pronto, OrdenacaoProcessos.PRIORIDADE);
                executando = pronto.get(0);
                pronto.remove(0);

                //calcula a execução do processo atual
                executando.setEspera(tempoAtual - executando.getChegada());
                tempoAtual += executando.getDuracao();
                executando.setTurnaround(tempoAtual - executando.getChegada());

                //adiciona ao Arry de saída
                resultado.add(executando);
                //atualiza o tempo de termino de execução no gráfico

                mEspera += executando.getEspera();
                mTurnAround += executando.getTurnaround();
            }
            else
            {
                //há um intervalode tempo "livre", onde não há processos para executar
                if(!entrada.isEmpty())
                {
                    //aponta para o próximo da fila para pegar o tempo de chegada
                    int tempo = entrada.get(0).getChegada();
                    tempoAtual = tempo;
                }
            }
        }
        //calcula os tempos médios de espera e turnaround
        mEspera /= resultado.size();
        mTurnAround /= resultado.size();
    }

    /**
     * Escalonador por Prioridade Preemptivo
     * Faz o escalonamento preemptivo levando em consideração a chegada e tendo
     * como prioridade o valor de prioridade dos processos.
     * Valoresultado maioresultado de prioridade indicam maior importância.
     */
    public void escalonarPrioridadeP() {
        int tempoAtual;
        Processo executando = null;

        //se não há um conjunt de processos válidos não escalona
        if(!validaEntrada(entrada))
            return;

        resultado = new ArrayList<>();
        pronto = new ArrayList<>();

        ordenar(entrada, OrdenacaoProcessos.CHEGADA);

        mEspera = 0;
        mTurnAround = 0;
        tempoAtual = entrada.get(0).getChegada();
        pronto.add(entrada.get(0));
        entrada.remove(0);

        //Enquanto exixtem processos na fila de entrada ou de Prontos ou o último processo não acabou
        while (!pronto.isEmpty() || !entrada.isEmpty() || executando != null)
        {
            //se chegaram novos processos, coloca na fila de prontos
            while (!entrada.isEmpty() && entrada.get(0).getChegada() <= tempoAtual)
            {
                if (entrada.get(0).getChegada() <= tempoAtual)
                {
                    pronto.add(entrada.get(0));
                    entrada.remove(0);
                    ordenar(pronto, OrdenacaoProcessos.PRIORIDADE);
                }
            }

            //verifica se tem processos na fila de prontos OU algum executando
            if (!pronto.isEmpty() || executando != null)
            {
                //Verifica se há prioridade para interromper
                if (executando != null) //se tem processo executando
                {
                    //processo na fila é de menor duração? Sim = troca
                    if (!pronto.isEmpty())
                        if (executando.getPrioridade() < pronto.get(0).getPrioridade())
                        {
                            executando.setInter(tempoAtual);//tempo onde foi interrompido
                            pronto.add(executando);
                            executando = pronto.get(0);
                            pronto.remove(0);
                            ordenar(pronto, OrdenacaoProcessos.PRIORIDADE);
                        }
                }
                else //pega o primeiro dos prontos
                {
                    executando = pronto.get(0);
                    pronto.remove(0);
                    //atualiza o tempo de espera do processo
                    executando.setEspera(executando.getEspera() + tempoAtual - executando.getInter());
                }
                //calcula a execução do processo atual
                tempoAtual++;
                executando.DecDuracao();

                //se terminou adiciona ao Arry de saída
                if (executando.getDuracao() == 0)
                {
                    executando.setTurnaround(tempoAtual - executando.getChegada());
                    resultado.add(executando);
                    //atualiza o tempo de termino de execução no gráfico

                    mEspera += executando.getEspera();
                    mTurnAround += executando.getTurnaround();
                    executando = null;
                }
            }
            else
            {
                 //há um intervalode tempo "livre", onde não há processos para executar
                if(!entrada.isEmpty())
                {
                    //aponta para o próximo da fila para pegar o tempo de chegada
                    int tempo = entrada.get(0).getChegada();
                    tempoAtual = tempo;
                }
            }
        }
        //calcula os tempos médios de espera e turnaround
        mEspera /= resultado.size();
        mTurnAround /= resultado.size();
    }

    /**
     * Escalonador Round Robin
     * Faz o escalonamento preemptivo Round Robin levando em consideração a
     * ordem de chegada dos processos.
     * @param quantum valor do quantum a ser utilizado no escalonamento
     */
    public void escalonarRR(int quantum) {
        int tempoAtual;
        Processo executando = null;

        //se não há um conjunt de processos válidos não escalona
        if(!validaEntrada(entrada) || quantum <= 0)
            return;

        resultado = new ArrayList<>();
        pronto = new ArrayList<>();

        ordenar(entrada, OrdenacaoProcessos.CHEGADA);

        mEspera = 0;
        mTurnAround = 0;
        tempoAtual = entrada.get(0).getChegada();
        pronto.add(entrada.get(0));
        entrada.remove(0);

        //Enquanto existem processos na fila de entrada ou de Prontos ou o último processo não acabou
        while (!pronto.isEmpty() || !entrada.isEmpty() || executando != null)
        {
            //se chegaram novos processos, coloca na fila de prontos
            while (!entrada.isEmpty() && entrada.get(0).getChegada() <= tempoAtual)
            {
                if (entrada.get(0).getChegada() <= tempoAtual)
                {
                    pronto.add(entrada.get(0));
                    entrada.remove(0);
                }
            }

            //verifica se tem processos na fila de prontos OU algum executando
            if (!pronto.isEmpty() || executando != null)
            {
                if (executando != null) //se tem processo executando
                {
                    //tem processo na fila de pronto? Sim = Troca
                    if (!pronto.isEmpty())
                    {
                        executando.setInter(tempoAtual);//tempo onde foi interrompido
                        pronto.add(executando);
                        executando = pronto.get(0);
                        pronto.remove(0);
                        //atualiza o tempo de espera do processo
                        executando.setEspera(executando.getEspera() + tempoAtual - executando.getInter());
                        //coloca no grafico e ajusta os valoresultado
                    }
                }
                else
                {
                    //pega o primeiro dos prontos caso exista
                    if (!pronto.isEmpty())
                    {
                        executando = pronto.get(0);
                        pronto.remove(0);
                        //atualiza o tempo de espera do processo
                        executando.setEspera(executando.getEspera() + tempoAtual - executando.getInter());
                    }
                }
                //calcula a execução do processo atual

                if (executando.getDuracao() > quantum)
                {
                    tempoAtual += quantum;
                    executando.DecDuracao(quantum);
                }
                else
                {
                    tempoAtual += executando.getDuracao();
                    executando.DecDuracao(executando.getDuracao());
                }

                //se terminou adiciona ao Arry de saída
                if (executando.getDuracao() == 0)
                {
                    executando.setTurnaround(tempoAtual - executando.getChegada());
                    resultado.add(executando);
                    mEspera += executando.getEspera();
                    mTurnAround += executando.getTurnaround();
                    executando = null;
                }
            }
            else
            {
                 //há um intervalode tempo "livre", onde não há processos para executar
                if(!entrada.isEmpty())
                {
                    //aponta para o próximo da fila para pegar o tempo de chegada
                    int tempo = entrada.get(0).getChegada();
                    tempoAtual = tempo;
                }
            }

        }
        //calcula os tempos médios de espera e turnaround
        mEspera /= resultado.size();
        mTurnAround /= resultado.size();
    }

	public List<Processo> getResultado() {
		return resultado;
	}

    /**
     * Retorna uma lista de processos ordenada pelo nome dos processos.
     * Estes processos armazenam seus tempo de espera e turnaround durante o escalonamento
     * @return ArrayList de processos ou null
     */	
}
