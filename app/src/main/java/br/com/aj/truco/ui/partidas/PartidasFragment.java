package br.com.aj.truco.ui.partidas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.adapter.PartidaJogadoresAdapter;
import br.com.aj.truco.adapter.PartidasAdapter;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentEstatisticaBinding;
import br.com.aj.truco.databinding.FragmentPartidasBinding;
import br.com.aj.truco.generic.RecyclerViewListenerHack;
import br.com.aj.truco.ui.EstatisticaJogadorGraficoActivity;
import br.com.aj.truco.ui.PartidaPontosActivity;
import br.com.aj.truco.ui.estatistica.EstatisticaViewModel;
import br.com.aj.truco.util.SharedPreferencesUtil;


public class PartidasFragment extends Fragment {


    private FragmentPartidasBinding binding;

    private RecyclerView recyclerView;
    private Activity activity;
    private PartidasAdapter adapter;


    public PartidasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPartidasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = getActivity();

        recyclerView = binding.partidasRecycleview;


        AppRoomDatabase dbs = AppRoomDatabase.getDatabase(getContext());

        List<Partida> partidaList = dbs.partidaDAO().getAll();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new PartidasAdapter(activity, partidaList, listClickListener, null);
        recyclerView.setAdapter(adapter);


        binding.partidasButtomTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Partida> partidaList = dbs.partidaDAO().getTotalPartidas();

                adapter = new PartidasAdapter(activity, partidaList, null, null);
                recyclerView.setAdapter(adapter);

            }
        });

 binding.partidasButtomTodas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Partida> partidaList = dbs.partidaDAO().getAll();

                adapter = new PartidasAdapter(activity, partidaList, listClickListener, null);
                recyclerView.setAdapter(adapter);

            }
        });

 binding.partidasButtomUltimas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Partida> partidaList = dbs.partidaDAO().getLastPartidas(3);

                adapter = new PartidasAdapter(activity, partidaList, null, null);
                recyclerView.setAdapter(adapter);

            }
        });


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private RecyclerViewListenerHack.OnClickListener listClickListener = new RecyclerViewListenerHack.OnClickListener<Object>() {
        @Override
        public void onClickListener(View view, int position, Object object) {

            if (object instanceof Partida && ((Partida) object).getPartidaID() != 0) {

                Intent intent = new Intent(getActivity(), PartidaPontosActivity.class);
                intent.putExtra(Partida.EXTRA_KEY,  ((Partida) object).getPartidaID());
                startActivity(intent);


            }
        }
    };

}