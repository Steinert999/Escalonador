package application;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Escalonador {
	
	private Deque<Processo> entry;
	private Deque<Processo> result;
	private Deque<Processo> ready;
	
	Logger logger = Logger.getLogger("LOGGER");
	
	public Escalonador(Deque<Processo> list) {
		setEntrada(list);
	}
	
	public static boolean validateEntry(Deque<Processo> lista) {
		if(lista == null || lista.isEmpty()) {
			return false;
		}
		if(lista.stream().anyMatch(p -> p.getChegada() < 0  || p.getDuracao() < 0 || p.getPrioridade() < 0)) {
			return false;
		}
		return true;
	}
	
	public void setEntrada(Deque<Processo> lista) {
		if(!validateEntry(lista)) return;
		entry = lista;
	}
	
	public Deque<Processo> sort(Deque<Processo> p , OrdenacaoProcessos ord) {
		
		switch (ord) {
		case CHEGADA:
			return p.stream().sorted(Comparator.comparing(Processo::getChegada)).collect(Collectors.toCollection(ArrayDeque::new));
		case DURACAO:
			return  p.stream().sorted(Comparator.comparing(Processo::getDuracao)).collect(Collectors.toCollection(ArrayDeque::new));
		case PRIORIDADE: // decrescente
			return p.stream().sorted(Comparator.comparing(Processo::getPrioridade).reversed()).collect(Collectors.toCollection(ArrayDeque::new));
		case NOME:
			return p.stream().sorted(Comparator.comparing(Processo::getId)).collect(Collectors.toCollection(ArrayDeque::new));
		}
		return null;
	}
	
	
	  /**
     * Escalonador First Came, First Served (FCFS)
     * Faz o escalonamento n�o preemptivo, por ordem de chegada.
     */
    public void escalonarFCFS() {

    	// se n�o h� processos validos n�o escalona
        if(!validateEntry(entry))
            return;
        
        logger.info("===================== ESCALONAMENTO POR ORDEM DE CHEGADA =====================");
        
        
        ready = new ArrayDeque<>();
        result = new ArrayDeque<>();
        Processo running;

        Long tempoAtual;

        this.entry = sort(entry, OrdenacaoProcessos.CHEGADA);
        
        tempoAtual = entry.getFirst().getChegada();
        
        entry.stream().forEach(p ->  {
        	logInitProcess(p);
        });
        ready.add(entry.getFirst());
        entry.removeFirst();
                
         //Enquanto existem processos na fila de entrada ou de Prontos
        while (!ready.isEmpty() || !entry.isEmpty()) {
        	
        	putEntryProcess(tempoAtual);
        	
            if (!ready.isEmpty()) {
            	
               this.ready = sort(ready, OrdenacaoProcessos.CHEGADA);
               
                running = ready.getFirst();
                
                logRunningProcess(running);
                
                ready.removeFirst();
                
                calculateRuntimeProcess(running, tempoAtual);
                
                logSucess(running);
                
                result.add(running);

            } else if(!entry.isEmpty()) {
                	Long time = entry.getFirst().getChegada();
                    tempoAtual = time;
                }
        }
    }

	/**
     * Escalonador Sortest Job First (SJF) Não-Preemptivo
     * Faz o escalonamento não preemptivo levando em consideração a chegada e tendo
     * como prioridade os processos de menor duração.
     */
    public void escalonarSJFnP() {
    	Long tempoAtual;
        Processo running;

        //se não há um conjunt de processos válidos não escalona
        if(!validateEntry(entry))
            return;
        
        logger.info("===================== ESCALONAMENTO POR DURA��O =====================");

        result = new ArrayDeque<>();
        ready = new ArrayDeque<>();

        this.entry = sort(entry, OrdenacaoProcessos.CHEGADA);

        tempoAtual = entry.getFirst().getChegada();
        entry.stream().forEach(p ->  {
        	logInitProcess(p);
        });
        ready.add(entry.getFirst());
        entry.removeFirst();

        //Enquanto existem processos na fila de entrada ou de Prontos
        while (!ready.isEmpty() || !entry.isEmpty())
        {
            //se chegaram novos processos, coloca na fila de prontos
        	putEntryProcess(tempoAtual);        	
            //verifica se existem processos na fila de prontos
            if (!ready.isEmpty())
            {
                //pega o próximo processo de menor duração na fila de prontos
                this.ready = sort(ready, OrdenacaoProcessos.DURACAO);
                running = ready.getFirst();
                ready.removeFirst();
                
                logRunningProcess(running);

                calculateRuntimeProcess(running, tempoAtual);

                logSucess(running);
                
                result.add(running);                
            }
            else
            {
                 //há um intervalode tempo "livre", onde não há processos para executar
                if(!entry.isEmpty())
                {
                    //aponta para o próximo da fila para pegar o tempo de chegada
                	Long time = entry.getFirst().getChegada();
                    tempoAtual = time;
                }
            }
        }
    }

    /**
     * Escalonador por Prioridade n�o-Preemptivo
     * Faz o escalonamento n�o preemptivo levando em considera��o a chegada e tendo
     * como prioridade o valor de prioridade dos processos.
     * Valor resultado maior resultado de prioridade indicam maior importancia.
     */
    public void escalonarPrioridadeNP() {
    	Long tempoAtual;
        Processo running;

        //se não há um conjunt de processos válidos não escalona
        if(!validateEntry(entry))
            return;
        
        logger.info("===================== ESCALONAMENTO POR PRIORIDADE =====================");

        result = new ArrayDeque<>();
        ready = new ArrayDeque<>();

        this.entry = sort(entry, OrdenacaoProcessos.CHEGADA);

        entry.stream().forEach(p -> logInitProcess(p));
        
        tempoAtual = entry.getFirst().getChegada();
        ready.add(entry.getFirst());
        entry.removeFirst();

        //Enquanto existem processos na fila de entrada ou de Prontos
        while (!ready.isEmpty() || !entry.isEmpty())
        {
            //se chegaram novos processos, coloca na fila de prontos
        	putEntryProcess(tempoAtual);
            if (!ready.isEmpty())
            {
                //pega o próximo processo de maior prioridade na fila de prontos
                this.ready = sort(ready, OrdenacaoProcessos.PRIORIDADE);
                
                running = ready.getFirst();
                
                ready.removeFirst();
                
                logRunningProcess(running);
                
                calculateRuntimeProcess(running, tempoAtual);
                
                logSucess(running);
                
                result.add(running);
            }
            else
            {
                //há um intervalode tempo "livre", onde não há processos para executar
                if(!entry.isEmpty())
                {
                    //aponta para o próximo da fila para pegar o tempo de chegada
                	Long time = entry.getFirst().getChegada();
                    tempoAtual = time;
                }
            }
        }

    }
	
	public void calculateRuntimeProcess(Processo p, Long runtime) {
		Long value =  p.getChegada() - runtime;
		p.setEspera(value);
		
		runtime += p.getDuracao();
		
		logger.info("Tempo de espera do Processo: " + runtime/1000000000);
		System.out.println();
		
		p.setTurnaround(runtime - p.getChegada());
	}
	
	public void putEntryProcess(Long runtime) {
		while (!entry.isEmpty() && entry.getFirst().getChegada() <= runtime) {
            if (entry.getFirst().getChegada() <= runtime) {
                ready.add(entry.getFirst());
                entry.removeFirst();
            }
        }
	}
	
	
    private void logInitProcess(Processo p) {
    	logger.info("Iniciando o processamento do Processo " + p.getId());
    	System.out.println();
    }
    
    private void logRunningProcess(Processo p ) {
    	 System.out.println();
    	 logger.info("Processo " + p.getId() + " em execu��o");
         System.out.println();
    }
    private void logSucess(Processo p ) {
    	logger.info("Processo " + p.getId() + " finalizado em " + 
    	LocalTime.now().format(DateTimeFormatter.ofPattern("mm:ss.SSS")));
	}
    
	public Deque<Processo> getResultado() {
		return result;
	}
}
