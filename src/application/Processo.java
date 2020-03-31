package application;


public class Processo {
	 	private final Long id;
	    private Long duracao,  chegada, prioridade;
	    private Long espera, turnaround;
	    private Long inter;

	    /**
	     * instancia um novo Processos.
	     * @param id id do Processos
	     * @param chegada tempo de chegada do Processos
	     * @param duracao tempo de duração do Processos
	     */
	    public Processo(Long id, Long chegada, Long duracao){
	        this.id = id;
	        this.chegada = chegada;
	        this.duracao = duracao;
	        this.prioridade = 0L;
	        espera = turnaround = 0L;
	        inter = chegada;
	    }

	    /**
	     * instancia um novo Processos.
	     * @param id
	     * @param chegada
	     * @param duracao
	     * @param prioridade
	     */
	    public Processo(Long id, Long chegada,Long duracao, Long prioridade) {
	        this.id = id;
	        this.chegada = chegada;
	        this.duracao = duracao;
	        this.prioridade = prioridade;
	        espera = turnaround = 0L;
	        inter = chegada;
	    }
	    
	    /**
	     * instancia um novo Processos.
	     * @param p Processos: um Processos
	     */
	    public Processo(Processo p)
	    {
	        this.id = p.id;
	        this.chegada = p.chegada;
	        this.duracao = p.duracao;
	        this.prioridade = p.prioridade;
	        espera = turnaround = 0L;
	        inter = p.chegada;
	    }

	    /**
	     * Recebe como parâmentro um Processos e verifica se ele é igual ao Processos
	     * que chamou o método. Igual significa que todos os dados são iguais (ID,
	     * Chegada, Duração e Prioridade), não quer dizer que sejam o mesmo objeto.
	     * @param p Processos a ser comparado
	     * @return true (se iguais) / false (caso contrário)
	     */
	    public boolean igual(Processo p)
	    {
	        if(p.getId() == (this.getId()) && p.getChegada() == this.getChegada()
	                && p.getDuracao()==this.getDuracao() && p.getPrioridade() == this.getPrioridade())
	            return true;
	        else
	            return false;
	    }
	    
	    /**
	     * @return o id (código) do Processos
	     */
	    public Long getId() {
	        return id;
	    }

	    /**
	     * @return a duracao do Processos
	     */
	    public Long getDuracao() {
	        return duracao;
	    }

	    /**
	     * decrementa uma unidade de tempo da duração de um Processos.
	     */
	    public void DecDuracao(){
	        duracao--;
	    }

	    /**
	     * decrementa "tempo" da duração de um Processos.
	     * @param tempo
	     */
	    public void DecDuracao(int tempo){
	        duracao -= tempo;
	    }

	    /**
	     * @return o tempo de chegada do Processos
	     */
	    public Long getChegada() {
	        return chegada;
	    }

	    /**
	     * @return o valor da espera
	     */
	    public Long getEspera() {
	        return espera;
	    }

	    /**
	     * @param espera o valor da espera
	     */
	    public void setEspera(Long espera) {
	        this.espera = espera;
	    }

	    /**
	     * @return o turnaround
	     */
	    public Long getTurnaround() {
	        return turnaround;
	    }

	    /**
	     * @param turnaround o turnaround a ser atribuído
	     */
	    public void setTurnaround(Long turnaround) {
	        this.turnaround = turnaround;
	    }

	    /**
	     * @return a prioridade
	     */
	    public Long getPrioridade() {
	        return prioridade;
	    }

	    /**
	     * @return the inter
	     */
		public Long getInter() {
			return inter;
		}
		
		/**
	     * @param inter the inter to set
	     */
	    public void setInter(Long inter) {
	        this.inter = inter;
	    }
}
