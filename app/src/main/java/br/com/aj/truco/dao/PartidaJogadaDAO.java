package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaPontos;
import br.com.aj.truco.classe.TimesPartida;


@Dao
public interface PartidaJogadaDAO {

    @Query("SELECT * FROM PartidaJogada")
    List<PartidaJogada> getAll();

    @Query("SELECT * FROM PartidaJogada WHERE PartidaJogadaID = :partidajogadaid")
    PartidaJogada getByID(long partidajogadaid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid ORDER BY PartidaJogadaID")
    List<PartidaJogada> getByPartida(long partidaid);

    @Query("SELECT p.PartidaJogadaID, " +
            " p.PartidaID, " +
            " p.JogadorID, " +
            " p.Pontos, " +
            " p.TimeID, " +
            " (p.PontosTime1 + (" +
            "  CASE WHEN (p.TimeID = 1 AND p.Vitoria) OR (p.TimeID = 2 AND NOT p.Vitoria)  THEN" +
            "    Pontos " +
            "   ELSE " +
            "     0 END) ) as PontosTime1, " +
            " (p.PontosTime2 + (" +
            "  CASE WHEN (p.TimeID = 2 AND p.Vitoria) OR (p.TimeID = 1 AND NOT p.Vitoria) THEN" +
            "    Pontos " +
            "   ELSE " +
            "     0 END)) as PontosTime2, " +
            " p.VitoriasTime1, " +
            " p.VitoriasTime2, " +
            " p.Vitoria, " +
            " (SELECT j.Nome FROM Jogador j WHERE j.JogadorID = p.JogadorID) as JogadorNome, " +
            " (SELECT t1.NomeTime1 FROM Partida t1 WHERE t1.PartidaID = :partidaid) as NomeTime1, " +
            " (SELECT t2.NomeTime2 FROM Partida t2 WHERE t2.PartidaID = :partidaid) as NomeTime2," +
            " (SELECT SUM(CASE WHEN pj2.Vitoria THEN pj2.Pontos ELSE pj2.Pontos * -1 END) FROM PartidaJogada pj2 WHERE pj2.PartidaID = p.PartidaID AND pj2.JogadorID = p.JogadorID and pj2.PartidaJogadaID <= p.PartidaJogadaID ) as JogadorPontos, " +
            " (SELECT SUM(CASE WHEN pj2.Vitoria THEN 1 ELSE -1 END) FROM PartidaJogada pj2 WHERE pj2.PartidaID = p.PartidaID AND pj2.JogadorID = p.JogadorID and pj2.PartidaJogadaID <= p.PartidaJogadaID ) as JogadorPartidas " +
            "  FROM PartidaJogada p " +
            " WHERE p.PartidaID = :partidaid " +
            "  ORDER BY p.PartidaJogadaID")
    List<PartidaPontos> getCompleteByPartida(long partidaid);


//    @Query("SELECT * FROM PartidaJogada WHERE TimeID = :timeid")
//    List<PartidaJogada> getByTime(int timeid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid AND JogadorID = :jogadorid")
    List<PartidaJogada> getByPartidaJogador(long partidaid, long jogadorid);

    @Query("SELECT count(*) FROM PartidaJogada WHERE JogadorID = :jogadorid")
    long getCountByJogador(long jogadorid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid ORDER BY PartidaJogadaID DESC")
    PartidaJogada getLast(long partidaid);

    @Query("SELECT * FROM PartidaJogada " +
            " WHERE PartidaID = :partidaid " +
            " AND (CASE WHEN (TimeID = :time1ID AND Vitoria) OR (TimeID = :time2ID AND NOT Vitoria) THEN PontosTime1 + Pontos ELSE PontosTime2 + Pontos END) >=12 " +
            " ORDER BY PartidaJogadaID")
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
    int deleteByJogador(long jogadorID);

    @Query("DELETE FROM PartidaJogada WHERE PartidaID = :partidaID")
    int deleteByPartida(long partidaID);

    @Query("DELETE FROM PartidaJogada WHERE PartidaID = :partidaID AND JogadorID = :jogadorID")
    int deleteByPartidaJogador(long partidaID, long jogadorID);

    @Query("SELECT MIN(TimeID) as Time1ID, MAX(TimeID) as Time2ID  FROM PartidaJogada WHERE PartidaID = :partidaid")
    TimesPartida geTimesByPartida(long partidaid);

}
