package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaPontos;


@Dao
public interface PartidaJogadaDAO {

    @Query("SELECT * FROM PartidaJogada")
    List<PartidaJogada> getAll();

    @Query("SELECT p.PartidaJogadaID, " +
            " p.PartidaID, " +
            " p.JogadorID, " +
            " p.Pontos, " +
            " p.TimeID, " +
            " (p.PontosTime1 + (" +
            "  CASE WHEN (p.TimeID = (SELECT Time1ID FROM Partida WHERE PartidaID = :partidaid) AND p.Vitoria) OR (p.TimeID = (SELECT Time2ID FROM Partida WHERE PartidaID = :partidaid) AND NOT p.Vitoria)  THEN" +
            "    Pontos " +
            "   ELSE " +
            "     0 END) ) as PontosTime1, " +
            " (p.PontosTime2 + (" +
            "  CASE WHEN (p.TimeID = (SELECT Time2ID FROM Partida WHERE PartidaID = :partidaid) AND p.Vitoria) OR (p.TimeID = (SELECT Time1ID FROM Partida WHERE PartidaID = :partidaid) AND NOT p.Vitoria) THEN" +
            "    Pontos " +
            "   ELSE " +
            "     0 END)) as PontosTime2, " +
            " p.VitoriasTime1, " +
            " p.VitoriasTime2, " +
            " p.Vitoria, " +
            " (SELECT j.Nome FROM Jogador j WHERE j.JogadorID = p.JogadorID) as JogadorNome, " +
            " (SELECT Nome FROM Time WHERE TimeID IN (SELECT Time1ID FROM Partida WHERE PartidaID = :partidaid)) as NomeTime1, " +
            " (SELECT Nome FROM Time WHERE TimeID IN (SELECT Time2ID FROM Partida WHERE PartidaID = :partidaid)) as NomeTime2, " +
            " (SELECT SUM(CASE WHEN pj2.Vitoria THEN pj2.Pontos ELSE pj2.Pontos * -1 END) FROM PartidaJogada pj2 WHERE pj2.PartidaID = p.PartidaID AND pj2.JogadorID = p.JogadorID and pj2.PartidaJogadaID <= p.PartidaJogadaID ) as JogadorPontos, " +
            " (SELECT SUM(CASE WHEN pj2.Vitoria THEN 1 ELSE -1 END) FROM PartidaJogada pj2 WHERE pj2.PartidaID = p.PartidaID AND pj2.JogadorID = p.JogadorID and pj2.PartidaJogadaID <= p.PartidaJogadaID ) as JogadorPartidas " +
            "  FROM PartidaJogada p " +
            " WHERE p.PartidaID = :partidaid " +
            "  ORDER BY p.PartidaJogadaID")
    List<PartidaPontos> getCompleteByPartida(long partidaid);



    @Query("SELECT count(*) FROM PartidaJogada WHERE JogadorID = :jogadorid")
    long getCountByJogador(long jogadorid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid ORDER BY PartidaJogadaID DESC")
    PartidaJogada getLast(long partidaid);


    @Query("SELECT * FROM (" +
            "SELECT PartidaJogadaID " +
            " ,PartidaID" +
            " ,TimeID" +
            " ,JogadorID" +
            " ,Pontos" +
            " ,(CASE WHEN (TimeID = :time1ID AND Vitoria) OR (TimeID = :time2ID AND NOT Vitoria) THEN PontosTime1 + Pontos ELSE PontosTime1 END) AS PontosTime1" +
            " ,(CASE WHEN (TimeID = :time2ID AND Vitoria) OR (TimeID = :time1ID AND NOT Vitoria) THEN PontosTime2 + Pontos ELSE PontosTime2 END) AS PontosTime2" +
            " ,VitoriasTime1" +
            " ,VitoriasTime2" +
            " ,Vitoria" +
            " FROM PartidaJogada " +
            " WHERE PartidaID = :partidaid " +
            " ORDER BY PartidaJogadaID ) TABELA" +
            " WHERE PontosTime1 >= 12 OR PontosTime2 >= 12")
    List<PartidaJogada> getResultadosByPartida(long partidaid, long time1ID, long time2ID);

    @Insert
    long insert(PartidaJogada partidajogada);

    @Update
    void update(PartidaJogada partidajogada);


    @Delete
    void delete(PartidaJogada partidajogada);

    @Query("DELETE FROM PartidaJogada")
    int deleteAll();

    @Query("DELETE FROM PartidaJogada WHERE JogadorID = :jogadorID")
    void deleteByJogador(long jogadorID);


}
