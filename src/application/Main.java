package application;

import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {
	
	public static void main(String[] args) {
		
		Random gerador = new Random();
		
		Deque<Processo> d = Arrays.asList(
			new Processo(1l, 10l, 1l, OrdenacaoProcessos.CHEGADA.getValue()),
			new Processo(2l, 9l, 3l, OrdenacaoProcessos.PRIORIDADE.getValue()),
			new Processo(3l, 3l, 11l, OrdenacaoProcessos.DURACAO.getValue()),
			new Processo(4l, 9l, 2l, OrdenacaoProcessos.DURACAO.getValue())
		).stream().collect(Collectors.toCollection(ArrayDeque::new));
		
		Escalonador e = new Escalonador(d);
		e.escalonarShortDuration();
		Armazenar.gravar(e.getResultado());
	}
}
