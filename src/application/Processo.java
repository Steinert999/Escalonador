package application;


public class Processo {
 	private final Long id;
    private Long duracao,  chegada, prioridade;
    private Long espera, turnaround;
    private Long inter;

    public Processo(Long id, Long chegada, Long duracao){
        this.id = id;
        this.chegada = chegada;
        this.duracao = duracao;
        this.prioridade = 0L;
        espera = turnaround = 0L;
        inter = chegada;
    }

    public Processo(Long id, Long chegada,Long duracao, Long prioridade) {
        this.id = id;
        this.chegada = chegada;
        this.duracao = duracao;
        this.prioridade = prioridade;
        espera = turnaround = 0L;
        inter = chegada;
    }
    
    public Processo(Processo p) {
        this.id = p.id;
        this.chegada = p.chegada;
        this.duracao = p.duracao;
        this.prioridade = p.prioridade;
        espera = turnaround = 0L;
        inter = p.chegada;
    }

    public boolean igual(Processo p) {
        if(p.getId() == (this.getId()) && p.getChegada() == this.getChegada()
                && p.getDuracao()==this.getDuracao() && p.getPrioridade() == this.getPrioridade())
            return true;
        else
            return false;
    }
    
    public Long getId() {
        return id;
    }

    public Long getDuracao() {
        return duracao;
    }

    public void DecDuracao(){
        duracao--;
    }

    public void DecDuracao(int tempo){
        duracao -= tempo;
    }

    public Long getChegada() {
        return chegada;
    }

    public Long getEspera() {
        return espera;
    }

    public void setEspera(Long espera) {
        this.espera = espera;
    }
    
    public Long getTurnaround() {
        return turnaround;
    }

    public void setTurnaround(Long turnaround) {
        this.turnaround = turnaround;
    }

    public Long getPrioridade() {
        return prioridade;
    }

	public Long getInter() {
		return inter;
	}
	
    public void setInter(Long inter) {
        this.inter = inter;
    }
}