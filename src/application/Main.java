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
			new Processo(1l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()) , Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3)) ),
			new Processo(2l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3))),
			new Processo(3l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3))),
			new Processo(4l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3))),
			new Processo(5l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3))),
			new Processo(6l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3))),
			new Processo(7l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3))),
			new Processo(8l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3))),
			new Processo(9l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3))),
			new Processo(10l, Long.valueOf(LocalTime.now().plusNanos(gerador.nextInt()).getNano()), Long.valueOf(gerador.nextInt(100)), Long.valueOf(gerador.nextInt(3)))
		).stream().collect(Collectors.toCollection(ArrayDeque::new));
		
		
		Escalonador e = new Escalonador(d);
		e.escalonarPrioridade();
		Armazenar.gravar(e.getResultado());
	}
}
