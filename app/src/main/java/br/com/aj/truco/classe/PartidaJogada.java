package br.com.aj.truco.classe;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "PartidaJogada")
public class PartidaJogada {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long PartidaJogadaID;
    private long PartidaID;
    //private int TimeID;
    private long JogadorID;
    private int Pontos;
    private int PontosTime1;
    private int PontosTime2;
    private int VitoriasTime1;
    private int VitoriasTime2;
    private boolean Vitoria;

    public long getPartidaJogadaID() {
        return PartidaJogadaID;
    }

    public void setPartidaJogadaID(long partidaJogadaID) {
        PartidaJogadaID = partidaJogadaID;
    }

    public long getPartidaID() {
        return PartidaID;
    }

    public void setPartidaID(long partidaID) {
        PartidaID = partidaID;
    }

//    public int getTimeID() {
//        return TimeID;
//    }
//
//    public void setTimeID(int timeID) {
//        TimeID = timeID;
//    }

    public long getJogadorID() {
        return JogadorID;
    }

    public void setJogadorID(long jogadorID) {
        JogadorID = jogadorID;
    }

    public int getPontos() {
        return Pontos;
    }

    public void setPontos(int pontos) {
        Pontos = pontos;
    }



    public int getPontosTime1() {
        return PontosTime1;
    }

    public void setPontosTime1(int pontosTime1) {
        PontosTime1 = pontosTime1;
    }

    public int getPontosTime2() {
        return PontosTime2;
    }

    public void setPontosTime2(int pontosTime2) {
        PontosTime2 = pontosTime2;
    }

    public int getVitoriasTime1() {
        return VitoriasTime1;
    }

    public void setVitoriasTime1(int vitoriasTime1) {
        VitoriasTime1 = vitoriasTime1;
    }

    public int getVitoriasTime2() {
        return VitoriasTime2;
    }

    public void setVitoriasTime2(int vitoriasTime2) {
        VitoriasTime2 = vitoriasTime2;
    }

    public boolean isVitoria() {
        return Vitoria;
    }

    public void setVitoria(boolean vitoria) {
        Vitoria = vitoria;
    }

}
