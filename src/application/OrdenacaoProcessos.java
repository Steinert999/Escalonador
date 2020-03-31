package application;

public enum OrdenacaoProcessos {
	CHEGADA(0L),DURACAO(1L),PRIORIDADE(2L),NOME(3L);
	
	private Long value;
	
	private OrdenacaoProcessos(Long value) {
		this.value = value;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}	
}
