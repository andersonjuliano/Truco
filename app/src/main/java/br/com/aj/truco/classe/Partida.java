package br.com.aj.truco.classe;


import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

@Entity
public class Partida {

    @Ignore
    public static final String EXTRA_KEY = Partida.class.getName();

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long PartidaID;
    private long DataPartida;
    private int PontosTime1 = 0;
    private int PontosTime2 = 0;
    private int VitoriaTime1 = 0;
    private int VitoriaTime2 = 0;
    /**
     * Jogador Pé (Ativo)
     */
    private long JogadorID = 0;
    private String NomeTime1;
    private String NomeTime2;

    //region get/set
    public long getPartidaID() {
        return PartidaID;
    }

    public void setPartidaID(long partidaID) {
        PartidaID = partidaID;
    }

    public long getDataPartida() {
        return DataPartida;
    }

    public void setDataPartida(long dataPartida) {
        DataPartida = dataPartida;
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

    public long getJogadorID() {
        return JogadorID;
    }

    public void setJogadorID(long jogadorID) {
        JogadorID = jogadorID;
    }

    public String getNomeTime1() {
        return NomeTime1;
    }

    public void setNomeTime1(String pNomeTime1) {
        NomeTime1 = pNomeTime1;
    }

    public String getNomeTime2() {
        return NomeTime2;
    }

    public void setNomeTime2(String pNomeTime2) {
        NomeTime2 = pNomeTime2;
    }

    //region versão 10
    private long Time1ID;
    private long Time2ID;

    public long getTime1ID() {
        return Time1ID;
    }

    public void setTime1ID(long pTime1ID) {
        Time1ID = pTime1ID;
    }

    public long getTime2ID() {
        return Time2ID;
    }

    public void setTime2ID(long pTime2ID) {
        Time2ID = pTime2ID;
    }
    //endregion

    //endregion get/set

    //region Métodos auxiliares

    public void SomarPontoTime1(Integer ponto) {
        if (ponto == null) {
            PontosTime1 += 1;
        } else {
            PontosTime1 += ponto;
        }
        if (PontosTime1 >= 12) {
            PontosTime2 = 0;
            PontosTime1 = 0;
            VitoriaTime1 += 1;
        }

    }

    public void SomarPontoTime2(Integer ponto) {
        if (ponto == null) {
            PontosTime2 += 1;
        } else {
            PontosTime2 += ponto;
        }
        if (PontosTime2 >= 12) {
            PontosTime2 = 0;
            PontosTime1 = 0;
            VitoriaTime2 += 1;
        }
    }

    public String getTitulo() {

        return String.valueOf(PartidaID) + " - " + DateFormat.format("dd/MM/yyyy", new Date(DataPartida)).toString();

    }

    public String getDiaMes() {

        return DateFormat.format("dd/MM", new Date(DataPartida)).toString();

    }

    public int getDia() {

        return Integer.parseInt(DateFormat.format("dd", new Date(DataPartida)).toString());

    }

    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PartidaID", PartidaID);
            jsonObject.put("DataPartida", DataPartida);
            jsonObject.put("PontosTime1", PontosTime1);
            jsonObject.put("PontosTime2", PontosTime2);
            jsonObject.put("VitoriaTime1", VitoriaTime1);
            jsonObject.put("VitoriaTime2", VitoriaTime2);
            jsonObject.put("JogadorID", JogadorID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //endregion


}
