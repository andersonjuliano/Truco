package br.com.aj.truco.classe;

import android.text.format.DateFormat;

import java.util.Date;

public class PartidaPesquisa extends Partida {
    public long DataPartidaInicio;
    public long DataPartidaFim;


    public String getTitulo() {

        if (getPartidaID() > 0)
            return String.valueOf(getPartidaID()) + " - " + DateFormat.format("dd/MM/yyyy", new Date(getDataPartida())).toString();
        else
            return DateFormat.format("dd/MM/yyyy", new Date(DataPartidaInicio)).toString() + " até " + DateFormat.format("dd/MM/yyyy", new Date(DataPartidaFim)).toString();

    }

}
