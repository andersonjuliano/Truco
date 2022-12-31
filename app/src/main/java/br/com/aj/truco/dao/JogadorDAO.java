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

    @Query("SELECT * FROM Jogador ORDER BY  Ordem")
    List<Jogador> getAll();

    @Query("SELECT JogadorID, TimeID, CASE WHEN Ordem = 0 THEN 99 ELSE Ordem END AS Ordem1, Ordem , Nome " +
            "FROM Jogador ORDER BY  Ordem1")
    List<Jogador> getAllOrdem();

    @Query("SELECT * FROM Jogador WHERE Ordem > 0")
    List<Jogador> getJogadoresAtivos();

    @Query("SELECT * FROM Jogador WHERE JogadorID = :JogadorID")
    Jogador getJogador(long JogadorID);

    @Query("SELECT * FROM Jogador WHERE Nome = :Nome")
    Jogador getJogadorByNome(String Nome);

 @Query("SELECT * FROM Jogador WHERE Ordem = :ordem")
    Jogador getJogadorByOrdem(int ordem);

    @Query("SELECT * FROM Jogador WHERE Ordem > 0 ORDER BY Ordem")
    Jogador getFirstJogador();

    @Query("SELECT MAX(Ordem) FROM Jogador")
    int getMaxOrdem();

    @Insert
    long insert(Jogador jogador);

    @Delete
    void delete(Jogador jogador);

    @Query("DELETE FROM Jogador")
    int deleteAll();

    @Update
    void update(Jogador jogador);
}
