package br.com.aj.truco.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.aj.truco.classe.Jogador;

@Dao
public interface JogadorDAO {

    @Query("SELECT * FROM Jogador ORDER BY JogadorID")
    List<Jogador> getAll();

    @Query("SELECT * FROM Jogador WHERE Ativo = 1 ORDER BY JogadorID")
    List<Jogador> getAllAtivos();

    @Query("SELECT JogadorID, TimeID, CASE WHEN Ordem = 0 THEN 99 ELSE Ordem END AS Ordem1, Ordem, Nome, Ativo " +
            "FROM Jogador ORDER BY  Ordem1")
    List<Jogador> getAllOrdem();

    @Query("SELECT JogadorID, TimeID, CASE WHEN Ordem = 0 THEN 99 ELSE Ordem END AS Ordem1, Ordem, Nome, Ativo " +
            "FROM Jogador WHERE Ativo == 1 ORDER BY  Ordem1")
    List<Jogador> getAllAtivosOrdem();

    @Query("SELECT * FROM Jogador WHERE Ativo = 1")
    List<Jogador> getJogadoresAtivos();

    @Query("SELECT * FROM Jogador WHERE JogadorID = :JogadorID")
    Jogador getJogador(long JogadorID);

    @Query("SELECT JogadorID, TimeID, Ordem, Nome, Ativo " +
            "FROM Jogador WHERE Ordem > 0 ORDER BY  Ordem")
    List<Jogador> getJogadoresNaPartida();

    @Query("SELECT COUNT(*) FROM Jogador")
    int CountAll();

    @Query("SELECT * FROM Jogador WHERE Nome = :Nome")
    Jogador getJogadorByNome(String Nome);

    @Query("SELECT * FROM Jogador WHERE Ordem = :ordem")
    Jogador getJogadorByOrdem(int ordem);

    @Query("SELECT * FROM Jogador WHERE Ordem > 0 ORDER BY Ordem")
    Jogador getFirstJogador();

    @Query("SELECT * FROM Jogador WHERE JogadorID in (SELECT JogadorID FROM PartidaJogada WHERE PartidaID = :partidaID AND TimeID = :timeID GROUP BY JogadorID)")
    List<Jogador> getJogadoresByPartidaTime(long partidaID, long timeID);

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
