package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.com.aj.truco.classe.PartidaJogada;


@Dao
public interface PartidaJogadaDAO {

    @Query("SELECT * FROM PartidaJogada")
    List<PartidaJogada> getAll();

    @Query("SELECT * FROM PartidaJogada WHERE PartidaJogadaID = :partidajogadaid")
    List<PartidaJogada> getByID(long partidajogadaid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid")
    List<PartidaJogada> getByPartida(long partidaid);

//    @Query("SELECT * FROM PartidaJogada WHERE TimeID = :timeid")
//    List<PartidaJogada> getByTime(int timeid);

    @Query("SELECT * FROM PartidaJogada WHERE PartidaID = :partidaid AND JogadorID = :jogadorid")
    List<PartidaJogada> getByTimeJogador(long partidaid, long jogadorid);

    @Query("SELECT * FROM PartidaJogada ORDER BY PartidaJogadaID DESC")
    PartidaJogada getLast();


    @Insert
    long insert(PartidaJogada partidajogada);

    @Delete
    void delete(PartidaJogada partidajogada);

    @Query("DELETE FROM PartidaJogada")
    int deleteAll();
}
