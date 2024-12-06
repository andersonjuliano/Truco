package br.com.aj.truco.ui;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.BaseActivity;
import br.com.aj.truco.R;
import br.com.aj.truco.adapter.PartidaPontosAdapter;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaPontos;
import br.com.aj.truco.dao.AppRoomDatabase;

public class PartidaPontosActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida_pontos);

        setDisplayHomeAsUpEnabled(true, true);
        long partidaID = getIntent().getLongExtra(Partida.EXTRA_KEY, 0);
        long jogadorID = getIntent().getLongExtra(Jogador.EXTRA_KEY, 0);

        AppRoomDatabase dbs = AppRoomDatabase.getDatabase(getBaseContext());


        RecyclerView recyclerView = findViewById(R.id.partida_pontos_recycleview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PartidaPontosActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        List<PartidaPontos> partidaPontosList = dbs.partidaJogadaDAO().getCompleteByPartida(partidaID);
        PartidaPontosAdapter adapter = new PartidaPontosAdapter(PartidaPontosActivity.this, partidaPontosList, null, null, jogadorID);
        recyclerView.setAdapter(adapter);


    }
}