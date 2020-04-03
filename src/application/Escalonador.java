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
		if(lista == null || lista.isEmpty()) return false;
		if(lista.stream().anyMatch(p -> p.getChegada() < 0  || p.getDuracao() < 0 || p.getPrioridade() < 0)) return false;
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
			return  p.stream().sorted(Comparator.comparing(Processo::getDuracao).reversed()).collect(Collectors.toCollection(ArrayDeque::new));
		case PRIORIDADE:
			return p.stream().sorted(Comparator.comparing(Processo::getPrioridade).reversed()).collect(Collectors.toCollection(ArrayDeque::new));
		case NOME:
			return p.stream().sorted(Comparator.comparing(Processo::getId)).collect(Collectors.toCollection(ArrayDeque::new));
		}
		return null;
	}
	
	// Escalonamento por ordem de chegada
    public void escalonadorArrivalOrder() {

        if(!validateEntry(entry))
            return;
        
        logger.info("===================== ESCALONAMENTO POR ORDEM DE CHEGADA =====================");
        ready = new ArrayDeque<>();
        result = new ArrayDeque<>();
        Processo running;
        Long currentTime;
        this.entry = sort(entry, OrdenacaoProcessos.CHEGADA);
        currentTime = entry.getFirst().getChegada();
        entry.stream().forEach(p ->  logInitProcess(p) );
        ready.add(entry.getFirst());
        entry.removeFirst();
                
        while (!ready.isEmpty() || !entry.isEmpty()) {
        	
        	putEntryProcess(currentTime);
        	
            if (!ready.isEmpty()) {            	
                this.ready = sort(ready, OrdenacaoProcessos.CHEGADA);               
                running = ready.getFirst();                
                logRunningProcess(running);                
                ready.removeFirst();                
                calculateRuntimeProcess(running, currentTime);            
                logSucess(running);                
                result.add(running);
            } else if(!entry.isEmpty()) {
            	Long time = entry.getFirst().getChegada();
                currentTime = time;
            }
        }
    }

    // Escalonador por menor duração
    public void escalonarShortDuration() {

        if(!validateEntry(entry))
            return;
        
        logger.info("===================== ESCALONAMENTO POR DURAÇÃO =====================");
        Long currentTime;
        Processo running;
        result = new ArrayDeque<>();
        ready = new ArrayDeque<>();
        this.entry = sort(entry, OrdenacaoProcessos.CHEGADA);
        currentTime = entry.getFirst().getChegada();
        entry.stream().forEach(p ->  logInitProcess(p) );
        ready.add(entry.getFirst());
        entry.removeFirst();

        while (!ready.isEmpty() || !entry.isEmpty()) {
        	putEntryProcess(currentTime);        	
            if (!ready.isEmpty()) {
                this.ready = sort(ready, OrdenacaoProcessos.DURACAO);
                running = ready.getFirst();
                logRunningProcess(running);
                ready.removeFirst();                
                calculateRuntimeProcess(running, currentTime);
                logSucess(running);                
                result.add(running);                
            }
            else{
                if(!entry.isEmpty()){
                	Long time = entry.getFirst().getChegada();
                    currentTime = time;
                }
            }
        }
    }

    // Escalonador por prioridade
    public void escalonarPrioridade() {

        if(!validateEntry(entry))
            return;
        
        logger.info("===================== ESCALONAMENTO POR PRIORIDADE =====================");
        Long tempoAtual;
        Processo running;
        result = new ArrayDeque<>();
        ready = new ArrayDeque<>();
        this.entry = sort(entry, OrdenacaoProcessos.CHEGADA);
        entry.stream().forEach(p -> logInitProcess(p));
        tempoAtual = entry.getFirst().getChegada();
        ready.add(entry.getFirst());
        entry.removeFirst();

        while (!ready.isEmpty() || !entry.isEmpty()){
        	putEntryProcess(tempoAtual);
            if (!ready.isEmpty()){
                this.ready = sort(ready, OrdenacaoProcessos.PRIORIDADE);               
                running = ready.getFirst();                
                ready.removeFirst();                
                logRunningProcess(running);                
                calculateRuntimeProcess(running, tempoAtual);                
                logSucess(running);                
                result.add(running);
            }
            else{
                if(!entry.isEmpty()){
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
    	 logger.info("Processo " + p.getId() + " em execução");
         System.out.println();
    }
    private void logSucess(Processo p ) {
    	logger.info("Processo " + p.getId() + " finalizado em " + 
    	LocalTime.now().format(DateTimeFormatter.ofPattern("mm:ss.SSS")));
	}
    
	public Deque<Processo> getResultado() {
		result.stream().map(Processo::toString).forEach(System.out::println);
		return result;
	}
}