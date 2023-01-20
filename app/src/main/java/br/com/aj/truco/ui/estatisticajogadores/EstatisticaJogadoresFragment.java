package br.com.aj.truco.ui.estatisticajogadores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.adapter.PartidaJogadoresAdapter;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentEstatisticaBinding;
import br.com.aj.truco.generic.RecyclerViewListenerHack;
import br.com.aj.truco.ui.EstatisticaJogadorGraficoActivity;
import br.com.aj.truco.ui.PartidaPontosActivity;
import br.com.aj.truco.ui.estatistica.EstatisticaViewModel;
import br.com.aj.truco.util.SharedPreferencesUtil;


public class EstatisticaJogadoresFragment extends Fragment {


    private RecyclerView recyclerView;
    private Activity activity;
    private PartidaJogadoresAdapter adapter;


    public EstatisticaJogadoresFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_estatistica_jogadores, container, false);

        activity = getActivity();

        recyclerView = rootView.findViewById(R.id.estatistica_jogadores_recycleview);


        AppRoomDatabase dbs = AppRoomDatabase.getDatabase(getContext());
        long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
        List<PartidaJogador> partidaJogadores = dbs.partidaJogadorDAO().getByPartida(partidaID);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new PartidaJogadoresAdapter(activity, partidaJogadores, listClickListener, null);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private RecyclerViewListenerHack.OnClickListener listClickListener = new RecyclerViewListenerHack.OnClickListener<Object>() {
        @Override
        public void onClickListener(View view, int position, Object object) {

            if (object instanceof PartidaJogador) {

                Intent intent = new Intent(getActivity(), PartidaPontosActivity.class);
                intent.putExtra(Partida.EXTRA_KEY,  ((PartidaJogador) object).getPartidaID());
                intent.putExtra(Jogador.EXTRA_KEY,  ((PartidaJogador) object).getJogadorID());
                startActivity(intent);

            }
        }
    };
}