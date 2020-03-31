package application;

import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {
	
	public static void main(String[] args) {
	 Deque<Processo> d = Arrays.asList(
				new Processo(1l, Long.valueOf(LocalTime.now().plusNanos(3000).getNano()) , 3l, OrdenacaoProcessos.DURACAO.getValue()),
				new Processo(2l, System.currentTimeMillis() * 2, 3l, OrdenacaoProcessos.NOME.getValue()),
				new Processo(3l, Long.valueOf(LocalTime.now().plusNanos(2000).getNano()), 1l, OrdenacaoProcessos.CHEGADA.getValue()),
				new Processo(4l, Long.valueOf(LocalTime.now().plusNanos(5000).getNano()), 4l, OrdenacaoProcessos.PRIORIDADE.getValue())
				)
			 .stream().collect(Collectors.toCollection(ArrayDeque::new));
		
		Escalonador e = new Escalonador(d);
		e.escalonarFCFS();
//		Armazenar.gravar(e.getResultado());
		e.escalonarPrioridadeNP();
//		Armazenar.gravar(e.getResultado());
		e.escalonarSJFnP();
//		Armazenar.gravar(e.getResultado());
		
	}
}
