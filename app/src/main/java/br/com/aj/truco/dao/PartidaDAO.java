package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaPesquisa;

@Dao
public interface PartidaDAO {


    @Query("SELECT * FROM Partida ORDER BY PartidaID DESC")
    List<Partida> getAll();

    @Query("SELECT p.*, DataPartida as DataPartidaInicio, DataPartida as DataPartidaFim FROM Partida p ORDER BY PartidaID DESC")
    List<PartidaPesquisa> getAllP();

    @Query("SELECT 0 as PartidaID " +
            ",MAX(DataPartida) as DataPartida" +
            ",MAX(DataPartida) as DataPartidaFim" +
            ",MIN(DataPartida) as DataPartidaInicio" +
            ",0 as JogadorID" +
            ",SUM(PontosTime1) as PontosTime1" +
            ",SUM(PontosTime2) as PontosTime2" +
            ",SUM(VitoriaTime1) as VitoriaTime1" +
            ",SUM(VitoriaTime2) as VitoriaTime2" +
            ",0 as Ativo" +
            " ,Time1ID" +
            " ,NomeTime1" +
            " ,Time2ID" +
            " ,NomeTime2" +
            " FROM (SELECT * FROM Partida ORDER BY PartidaID DESC LIMIT :limit)"+
            "GROUP BY Time1ID, Time2ID, NomeTime1, NomeTime2")
    List<PartidaPesquisa> getLastPartidas(int limit);


    @Query("SELECT 0 as PartidaID" +
            " ,MAX(DataPartida) as DataPartida" +
            " ,MAX(DataPartida) as DataPartidaFim" +
            " ,MIN(DataPartida) as DataPartidaInicio" +
            " ,0 as JogadorID" +
            " ,SUM(PontosTime1) as PontosTime1" +
            " ,SUM(PontosTime2) as PontosTime2" +
            " ,SUM(VitoriaTime1) as VitoriaTime1" +
            " ,SUM(VitoriaTime2) as VitoriaTime2" +
            " ,0 as Ativo" +
            " ,Time1ID" +
            " ,NomeTime1" +
            " ,Time2ID" +
            " ,NomeTime2" +
            " FROM Partida " +
            "GROUP BY Time1ID, Time2ID, NomeTime1, NomeTime2")
    List<PartidaPesquisa> getTotalPartidas();




    @Query("SELECT * FROM Partida WHERE PartidaID = :id")
    Partida getPartida(long id);

    @Query("SELECT * FROM Partida ORDER BY PartidaID DESC")
    Partida getLastPartida();

    @Query("SELECT * FROM Partida WHERE date(DataPartida / 1000,'unixepoch')  = date(:data / 1000,'unixepoch')")
    Partida getPartidasByData(long data);

    @Insert
    long insert(Partida partida);

    @Delete
    void delete(Partida partida);

    @Update
    void update(Partida partida);

    @Query("DELETE FROM Partida")
    int deleteAll();

    @Query("DELETE FROM Partida WHERE (PontosTime1 + PontosTime2 + VitoriaTime1 + VitoriaTime2) = 0 AND PartidaID != :partidaID")
    int deleteNull(long partidaID);

}
