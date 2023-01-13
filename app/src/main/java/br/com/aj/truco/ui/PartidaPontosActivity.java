package br.com.aj.truco.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import br.com.aj.truco.BaseActivity;
import br.com.aj.truco.R;
import br.com.aj.truco.adapter.PartidaJogadoresAdapter;
import br.com.aj.truco.adapter.PartidaPontosAdapter;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.PartidaPontos;
import br.com.aj.truco.classe.TimesPartida;
import br.com.aj.truco.dao.AppRoomDatabase;

public class PartidaPontosActivity extends BaseActivity {


    private RecyclerView recyclerView;
    private PartidaPontosAdapter adapter;
    private AppRoomDatabase dbs;
    private long partidaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida_pontos);

        setDisplayHomeAsUpEnabled(true, true);
        partidaID = getIntent().getLongExtra(Partida.EXTRA_KEY, 0);

        dbs = AppRoomDatabase.getDatabase(getBaseContext());


        recyclerView = findViewById(R.id.partida_pontos_recycleview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PartidaPontosActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        TimesPartida timesPartida = dbs.partidaJogadorDAO().geTimesByPartida(partidaID);
        List<PartidaJogada> partidaJogadaList = dbs.partidaJogadaDAO().getByPartida(partidaID);
        List<PartidaPontos> partidaPontosList = dbs.partidaJogadaDAO().getCompleteByPartida(partidaID, timesPartida.Time1ID, timesPartida.Time2ID);
        adapter = new PartidaPontosAdapter(PartidaPontosActivity.this, partidaPontosList, null, null);
        recyclerView.setAdapter(adapter);


    }
}