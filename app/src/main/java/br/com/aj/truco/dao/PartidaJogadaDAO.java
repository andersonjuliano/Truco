package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaPontos;


@Dao
public interface PartidaJogadaDAO {

    @Query("SELECT * FROM PartidaJogada")
    List<PartidaJogada> getAll();

    @Query("SELECT * FROM PartidaJogada WHERE PartidaJogadaID = :partidajogadaid")
    List<PartidaJogada> getByID(long partidajogadaid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid ORDER BY PartidaJogadaID")
    List<PartidaJogada> getByPartida(long partidaid);

    @Query("SELECT p.PartidaJogadaID, " +
            " p.PartidaID, " +
            " p.JogadorID, " +
            " p.Pontos, " +
            " pj.TimeJogadorID as TimeID, " +
            " (p.PontosTime1 + (" +
            "  CASE WHEN (pj.TimeJogadorID = 1 AND p.Vitoria) OR (pj.TimeJogadorID = 2 AND NOT p.Vitoria)  THEN" +
            "    Pontos " +
            "   ELSE " +
            "     0 END) ) as PontosTime1, " +
            " pj.TimeJogadorID as TimeID, " +
            " (p.PontosTime2 + (" +
            "  CASE WHEN (pj.TimeJogadorID = 2 AND p.Vitoria) OR (pj.TimeJogadorID = 1 AND NOT p.Vitoria) THEN" +
            "    Pontos " +
            "   ELSE " +
            "     0 END)) as PontosTime2, " +
            " p.VitoriasTime1, " +
            " p.VitoriasTime2, " +
            " p.Vitoria, " +
            " (SELECT j.Nome FROM Jogador j WHERE j.JogadorID = p.JogadorID) as JogadorNome, " +
            " (SELECT t1.Nome FROM Time t1 WHERE t1.TimeID = :time1id) as Time1Nome, " +
            " (SELECT t2.Nome FROM Time t2 WHERE t2.TimeID = :time2id) as Time2Nome," +
            " (SELECT SUM(CASE WHEN pj2.Vitoria THEN pj2.Pontos ELSE pj2.Pontos * -1 END) FROM PartidaJogada pj2 WHERE pj2.PartidaID = p.PartidaID AND pj2.JogadorID = p.JogadorID and pj2.PartidaJogadaID <= p.PartidaJogadaID ) as JogadorPontos, " +
            " (SELECT SUM(CASE WHEN pj2.Vitoria THEN 1 ELSE -1 END) FROM PartidaJogada pj2 WHERE pj2.PartidaID = p.PartidaID AND pj2.JogadorID = p.JogadorID and pj2.PartidaJogadaID <= p.PartidaJogadaID ) as JogadorPartidas " +
            "  FROM PartidaJogada p " +
            "  LEFT JOIN PartidaJogador pj on (p.JogadorID = pj.JogadorID AND p.PartidaID = pj.PartidaID) " +
            " WHERE p.PartidaID = :partidaid " +
            "  ORDER BY p.PartidaJogadaID")
    List<PartidaPontos> getCompleteByPartida(long partidaid, long time1id, long time2id);


//    @Query("SELECT * FROM PartidaJogada WHERE TimeID = :timeid")
//    List<PartidaJogada> getByTime(int timeid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid AND JogadorID = :jogadorid")
    List<PartidaJogada> getByTimeJogador(long partidaid, long jogadorid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid ORDER BY PartidaJogadaID DESC")
    PartidaJogada getLast(long partidaid);

    @Insert
    long insert(PartidaJogada partidajogada);

    @Delete
    void delete(PartidaJogada partidajogada);

    @Query("DELETE FROM PartidaJogada")
    int deleteAll();

    @Query("DELETE FROM PartidaJogada WHERE JogadorID = :jogadorID")
    int deleteByJogador(long jogadorID);

    @Query("DELETE FROM PartidaJogada WHERE PartidaID = :partidaID")
    int deleteByPartida(long partidaID);

    @Query("DELETE FROM PartidaJogada WHERE PartidaID = :partidaID AND JogadorID = :jogadorID")
    int deleteByPartidaJogador(long partidaID,long jogadorID);


}
