package br.com.aj.truco.classe;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class PartidaJogador {

    @Ignore
    public static final String EXTRA_KEY = PartidaJogador.class.getName();


    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long PartidaJogadorID;
    private long TimeJogadorID;
    private long PartidaID;
    private long JogadorID;
    private int Vitoria;
    private int Derrota;
    private int PontosGanhos;
    private int PontosPerdidos;


    public long getPartidaJogadorID() {
        return PartidaJogadorID;
    }

    public void setPartidaJogadorID(long partidaJogadorID) {
        PartidaJogadorID = partidaJogadorID;
    }

    public long getTimeJogadorID() {
        return TimeJogadorID;
    }

    public void setTimeJogadorID(long timeJogadorID) {
        TimeJogadorID = timeJogadorID;
    }

    public long getPartidaID() {
        return PartidaID;
    }

    public void setPartidaID(long partidaID) {
        PartidaID = partidaID;
    }

    public long getJogadorID() {
        return JogadorID;
    }

    public void setJogadorID(long jogadorID) {
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

    public int SaldoPontos() {
        return PontosGanhos - PontosPerdidos;
    }

    public int SaldoVitorias() {
        return Vitoria - Derrota;
    }


}
