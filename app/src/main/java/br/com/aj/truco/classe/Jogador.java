package br.com.aj.truco.classe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Jogador {
    @PrimaryKey
    private int JogadorID;
    private int Ordem;
    private String Nome;
    private int TimeID;

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

    public String getNome() {
        return Nome;
    }

    public int getOrdem() {
        return Ordem;
    }

    public void setOrdem(int ordem) {
        Ordem = ordem;
    }

    public void setNome(String nome) {
        Nome = nome;
    }
}
