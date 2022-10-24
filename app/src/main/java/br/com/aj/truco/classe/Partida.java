package br.com.aj.truco.classe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Partida {
    @PrimaryKey
    private int PartidaID;
    //public Date DataPartida;
    private int PontosTime1 = 0;
    private int PontosTime2 = 0;
    private int VitoriaTime1 = 0;
    private int VitoriaTime2 = 0;

    public int getPartidaID() {
        return PartidaID;
    }

    public void setPartidaID(int partidaID) {
        PartidaID = partidaID;
    }

//    public Date getDataPartida() {
//        return DataPartida;
//    }
//
//    public void setDataPartida(Date dataPartida) {
//        DataPartida = dataPartida;
//    }

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

    public int getVitoriaTime1() {
        return VitoriaTime1;
    }

    public void setVitoriaTime1(int vitoriaTime1) {
        VitoriaTime1 = vitoriaTime1;
    }

    public int getVitoriaTime2() {
        return VitoriaTime2;
    }

    public void setVitoriaTime2(int vitoriaTime2) {
        VitoriaTime2 = vitoriaTime2;
    }

    public void SomarPontoTime1(Integer ponto){
        if (ponto == null) {
            PontosTime1 += 1;
        }
        else{
            PontosTime1 += ponto;
        }
        if(PontosTime1 >= 12){
            PontosTime2 = 0;
            PontosTime1 = 0;
            VitoriaTime1 +=1;
        }

    }
    public void SomarPontoTime2(Integer ponto){
        if(ponto == null) {
            PontosTime2 += 1;
        }else{
            PontosTime2 += ponto;
        }
        if(PontosTime2 >= 12){
            PontosTime2 = 0;
            PontosTime1 = 0;
            VitoriaTime2 +=1;
        }
    }




}
