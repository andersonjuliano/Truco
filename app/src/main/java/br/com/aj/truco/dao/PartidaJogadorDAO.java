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


    @Query("SELECT * FROM PartidaJogador " +
            "WHERE PartidaID = :partidaid "
    )
        //"AND JogadorID in (SELECT JogadorID FROM JogadorDados WHERE MostrarEstatistica = 1) "
    List<PartidaJogador> getByPartida(long partidaid);

    @Query("SELECT * FROM PartidaJogador WHERE PartidaID = :partidaid AND JogadorID = :jogadorid")
    PartidaJogador getByJogadorPartida(long jogadorid, long partidaid);

    @Query("SELECT * FROM PartidaJogador WHERE JogadorID = :jogadorid ORDER BY PartidaID DESC LIMIT :limit")
    List<PartidaJogador> getLastByJogador(long jogadorid, int limit);


    @Query("SELECT MAX(PartidaJogadorID) AS PartidaJogadorID," +
            "      0 AS TimeJogadorID," +
            "      0 AS PartidaID," +
            "      JogadorID," +
            "      SUM(Vitoria) AS Vitoria," +
            "      SUM(Derrota) AS Derrota," +
            "      SUM(PontosGanhos) AS PontosGanhos," +
            "      SUM(PontosPerdidos) AS PontosPerdidos" +
            " FROM PartidaJogador " +
            "GROUP BY JogadorID")
        //    "WHERE JogadorID in (SELECT JogadorID FROM JogadorDados WHERE MostrarEstatistica = 1) " +
    List<PartidaJogador> getAllConsolidado();


    @Query("SELECT MAX(PartidaJogadorID) AS PartidaJogadorID," +
            "      0 AS TimeJogadorID," +
            "      0 AS PartidaID," +
            "      JogadorID," +
            "      SUM(Vitoria) AS Vitoria," +
            "      SUM(Derrota) AS Derrota," +
            "      SUM(PontosGanhos) AS PontosGanhos," +
            "      SUM(PontosPerdidos) AS PontosPerdidos" +
            " FROM PartidaJogador " +
            "WHERE PartidaID in (:partidasID) " +
            "GROUP BY JogadorID")
//            "  AND JogadorID in (SELECT JogadorID FROM JogadorDados WHERE MostrarEstatistica = 1) " +
    List<PartidaJogador> getByPartidas(List<Long> partidasID);


    @Insert
    long insert(PartidaJogador partidajogador);

    @Delete
    void delete(PartidaJogador partidajogador);

    @Update
    void update(PartidaJogador partidajogador);

    @Query("DELETE FROM PartidaJogador")
    int deleteAll();

    @Query("DELETE FROM PartidaJogador WHERE JogadorID = :jogadorID")
    int deleteByJogador(long jogadorID);

    @Query("DELETE FROM PartidaJogador WHERE JogadorID NOT IN (SELECT JogadorID  FROM Jogador)")
    int deleteJogadorExcluido();

    @Query("DELETE FROM PartidaJogador WHERE PartidaID  IN (SELECT PartidaID  FROM Partida WHERE (PontosTime1 + PontosTime2 + VitoriaTime1 + VitoriaTime2) = 0 AND PartidaID != :partidaID)")
    int deleteNull(long partidaID);

    @Query("DELETE FROM PartidaJogador WHERE (PontosGanhos + PontosPerdidos + Vitoria + Derrota) = 0")
    int deleteZero();


}
