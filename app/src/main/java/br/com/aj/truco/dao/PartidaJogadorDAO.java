package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


import br.com.aj.truco.classe.PartidaJogador;


@Dao
public interface PartidaJogadorDAO {

    @Query("SELECT * FROM PartidaJogador")
    List<PartidaJogador> getAll();

    @Query("SELECT * FROM PartidaJogador WHERE PartidaJogadorID = :partidajogadorid")
    PartidaJogador getByID(long partidajogadorid);

    @Query("SELECT * FROM PartidaJogador WHERE PartidaID = :partidaid")
    List<PartidaJogador> getByPartida(long partidaid);

    @Query("SELECT * FROM PartidaJogador WHERE PartidaID = :partidaid AND JogadorID = :jogadorid")
    PartidaJogador getByJogadorPartida(long jogadorid, long partidaid);

    @Query("SELECT MAX(PartidaJogadorID) AS PartidaJogadorID," +
            "      TimeJogadorID," +
            "      0 AS PartidaID," +
            "      JogadorID," +
            "      SUM(Vitoria) AS Vitoria," +
            "      SUM(Derrota) AS Derrota," +
            "      SUM(PontosGanhos) AS PontosGanhos," +
            "      SUM(PontosPerdidos) AS PontosPerdidos" +
            " FROM PartidaJogador GROUP BY TimeJogadorID, JogadorID")
    List<PartidaJogador> getAllConsolidado();


    @Query("SELECT MAX(PartidaJogadorID) AS PartidaJogadorID," +
            "      TimeJogadorID," +
            "      0 AS PartidaID," +
            "      JogadorID," +
            "      SUM(Vitoria) AS Vitoria," +
            "      SUM(Derrota) AS Derrota," +
            "      SUM(PontosGanhos) AS PontosGanhos," +
            "      SUM(PontosPerdidos) AS PontosPerdidos" +
            " FROM PartidaJogador " +
            "WHERE PartidaID in (:partidasID) " +
            "GROUP BY TimeJogadorID, JogadorID")
    List<PartidaJogador> getByPartidas(List<Long> partidasID);


    @Insert
    long insert(PartidaJogador partidajogador);

    @Delete
    void delete(PartidaJogador partidajogador);

    @Update
    void update(PartidaJogador partidajogador);

    @Query("DELETE FROM PartidaJogador")
    int deleteAll();
}
