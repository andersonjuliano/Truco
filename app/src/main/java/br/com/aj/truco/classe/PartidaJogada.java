package br.com.aj.truco.classe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PartidaJogada {

    @PrimaryKey
    private int PartidaJogadaID;
    private int PartidaID;
    private int TimeID;
    private int JogadorID;
    private int Pontos;

    public int getPartidaJogadaID() {
        return PartidaJogadaID;
    }

    public void setPartidaJogadaID(int partidaJogadaID) {
        PartidaJogadaID = partidaJogadaID;
    }

    public int getPartidaID() {
        return PartidaID;
    }

    public void setPartidaID(int partidaID) {
        PartidaID = partidaID;
    }

    public int getTimeID() {
        return TimeID;
    }

    public void setTimeID(int timeID) {
        TimeID = timeID;
    }

    public int getJogadorID() {
        return JogadorID;
    }

    public void setJogadorID(int jogadorID) {
        JogadorID = jogadorID;
    }

    public int getPontos() {
        return Pontos;
    }

    public void setPontos(int pontos) {
        Pontos = pontos;
    }
}
