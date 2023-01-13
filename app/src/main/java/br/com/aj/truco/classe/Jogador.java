package br.com.aj.truco.classe;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Jogador {

    @Ignore public static final String EXTRA_KEY = Jogador.class.getName();

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long JogadorID;
    private long TimeID;
    private int Ordem;
    private String Nome;

    public long getTimeID() {
        return TimeID;
    }

    public void setTimeID(long timeID) {
        TimeID = timeID;
    }

    public long getJogadorID() {
        return JogadorID;
    }

    public void setJogadorID(long jogadorID) {
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
