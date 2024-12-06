package br.com.aj.truco.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.adapter.PartidaPontosAdapter;
import br.com.aj.truco.classe.PartidaPontos;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class PartidaPontosFragment extends Fragment {

    private Activity activity;

    public PartidaPontosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_partida_pontos, container, false);

        activity = getActivity();


        AppRoomDatabase dbs = AppRoomDatabase.getDatabase(getContext());


        RecyclerView recyclerView = rootView.findViewById(R.id.partida_pontos_recycleview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);

        List<PartidaPontos> partidaPontosList = dbs.partidaJogadaDAO().getCompleteByPartida(partidaID);
        PartidaPontosAdapter adapter = new PartidaPontosAdapter(activity, partidaPontosList, null, null);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}