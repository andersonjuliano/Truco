package br.com.aj.truco.classe;

public class PartidaJogador {
    private int TimeJogadorID;
    private int PartidaID;
    private int JogadorID;
    private int Vitoria;
    private int Derrota;
    private int PontosGanhos;
    private int PontosPerdidos;

    public PartidaJogador(){
         Vitoria = 0;
       Derrota=0;
       PontosGanhos=0;
       PontosPerdidos=0;
    }

    public int getTimeJogadorID() {
        return TimeJogadorID;
    }

    public void setTimeJogadorID(int timeJogadorID) {
        TimeJogadorID = timeJogadorID;
    }

    public int getPartidaID() {
        return PartidaID;
    }

    public void setPartidaID(int partidaID) {
        PartidaID = partidaID;
    }

    public int getJogadorID() {
        return JogadorID;
    }

    public void setJogadorID(int jogadorID) {
        JogadorID = jogadorID;
    }

    public int getVitoria() {
        return Vitoria;
    }

    public void setVitoria(int vitoria) {
        Vitoria = vitoria;
    }

    public void somarVitoria() {
        Vitoria += 1;
    }
    public void deduzirVitoria() {
        Vitoria -= 1;
    }

    public int getDerrota() {
        return Derrota;
    }

    public void setDerrota(int derrota) {
        Derrota = derrota;
    }

    public void somarDerrota() {
        Derrota += 1;
    }
    public void deduzirDerrota() {
        Derrota -= 1;
    }

    public int getPontosGanhos() {
        return PontosGanhos;
    }

    public void setPontosGanhos(int pontosGanhos) {
        PontosGanhos = pontosGanhos;
    }

    public void somarPontosGanhos(int pontosGanhos) {
        PontosGanhos += pontosGanhos;
    }

    public int getPontosPerdidos() {
        return PontosPerdidos;
    }

    public void setPontosPerdidos(int pontosPerdidos) {
        PontosPerdidos = pontosPerdidos;
    }

    public void somarPontosPerdidos(int pontosPerdidos) {
        PontosPerdidos += pontosPerdidos;
    }
}
