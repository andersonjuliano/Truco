package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.aj.truco.classe.Time;

@Dao
public interface TimeDAO {

    @Query("SELECT * FROM Time")
    List<Time> getAll();

 @Query("SELECT * FROM Time WHERE Ativo = 1 ORDER BY TimeID")
    List<Time> getAtivos();

 @Query("SELECT * FROM Time WHERE Ativo = 1 ORDER BY TimeID DESC")
    List<Time> getAtivosAlt();

    @Query("SELECT * FROM Time WHERE TimeID = :timeid")
    Time getTime(long timeid);

    @Query("SELECT count(*) FROM PartidaJogada WHERE TimeID = :timeid")
    long getCountByTime(long timeid);

    @Query("SELECT COUNT(*) FROM Time")
    int CountAll();

    @Query("SELECT COUNT(*) FROM Time WHERE TimeID != :timeID AND Ativo = 1")
    int CountAtivos(long timeID);

    @Insert
    long insert(Time time);

    @Delete
    void delete(Time time);

    @Query("DELETE FROM Time")
    int deleteAll();

    @Update
    void update(Time time);
}
