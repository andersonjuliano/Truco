package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.aj.truco.classe.Partida;

@Dao
public interface PartidaDAO {


    @Query("SELECT * FROM Partida ORDER BY PartidaID DESC")
    List<Partida> getAll();

    @Query("SELECT 0 as PartidaID " +
            ",MAX(DataPartida) as DataPartida" +
            ",0 as JogadorID" +
            ",SUM(PontosTime1) as PontosTime1" +
            ",SUM(PontosTime2) as PontosTime2" +
            ",SUM(VitoriaTime1) as VitoriaTime1" +
            ",SUM(VitoriaTime2) as VitoriaTime2" +
            ",0 as Ativo" +
            ",MAX(Time1ID) as Time1ID" +
            ",MAX(Time2ID) as Time2ID" +
            " FROM (SELECT * FROM Partida ORDER BY PartidaID DESC LIMIT :limit)")
    List<Partida> getLastPartidas(int limit);


    @Query("SELECT 0 as PartidaID" +
            ",MAX(DataPartida) as DataPartida" +
            ",0 as JogadorID" +
            ",SUM(PontosTime1) as PontosTime1" +
            ",SUM(PontosTime2) as PontosTime2" +
            ",SUM(VitoriaTime1) as VitoriaTime1" +
            ",SUM(VitoriaTime2) as VitoriaTime2" +
            ",0 as Ativo" +
            ",MAX(Time1ID) as Time1ID" +
            ",MAX(Time2ID) as Time2ID" +
            " FROM Partida ")
    List<Partida> getTotalPartidas();




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
