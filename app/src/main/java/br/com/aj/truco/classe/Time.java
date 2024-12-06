package br.com.aj.truco.classe;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Time {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long TimeID;
    private String Nome;

    public long getTimeID() {
        return TimeID;
    }

    public void setTimeID(long timeID) {
        TimeID = timeID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    //region versão 10
    private Boolean Ativo;
    public Boolean isAtivo() {
        if (Ativo == null) {
            Ativo = false;
        }
        return Ativo;
    }
    public void setAtivo(Boolean ativo) {
        Ativo = ativo;
    }
    //endregion
}
