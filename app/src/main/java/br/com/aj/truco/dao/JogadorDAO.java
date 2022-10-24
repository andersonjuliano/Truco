package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import br.com.aj.truco.classe.Jogador;

@Dao
public interface JogadorDAO     {

    @Query("SELECT * FROM Jogador")
    List<Jogador> getJogadores();

    @Query("SELECT * FROM Jogador WHERE JogadorID = :JogadorID")
    List<Jogador> getJogador(int[] JogadorID);

    @Insert
    long insert(Jogador jogador);

    @Delete
    void delete(Jogador jogador);
}
