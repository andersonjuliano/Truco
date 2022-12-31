//package br.com.aj.truco.dao;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import java.util.List;
//
//import br.com.aj.truco.classe.Jogador;
//import br.com.aj.truco.classe.JogadorDados;
//
//@Dao
//public interface JogadorDadosDAO {
//
//    @Query("SELECT * FROM JogadorDados")
//    List<Jogador> getAll();
//
//    @Query("SELECT * FROM JogadorDados WHERE JogadorID = :JogadorID")
//    Jogador getJogador(long JogadorID);
//
//    @Insert
//    long insert(JogadorDados jogadorDados);
//
//    @Delete
//    void delete(JogadorDados jogadorDados);
//
//    @Query("DELETE FROM JogadorDados")
//    int deleteAll();
//
//    @Update
//    void update(JogadorDados jogadorDados);
//}
