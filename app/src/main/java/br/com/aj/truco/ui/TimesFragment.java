package br.com.aj.truco.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.adapter.TimesAdapter;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentTimesBinding;
import br.com.aj.truco.generic.RecyclerViewListenerHack;
import br.com.aj.truco.util.SharedPreferencesUtil;


public class TimesFragment extends Fragment {

    private FragmentTimesBinding binding;
    private RecyclerView recyclerView;
    private TimesAdapter adapter;
    private AppRoomDatabase dbs;
    private List<Time> times;
    private Time timeNovo;


    public TimesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTimesBinding.inflate(inflater, container, false);

        dbs = AppRoomDatabase.getDatabase(getContext());

        recyclerView = binding.timesRecycleview;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        binding.timesGravar.setOnClickListener(timesGravarClick);

        carregar();

        return binding.getRoot();

    }


    private void carregar() {
        times = dbs.timeDAO().getAll();
        adapter = new TimesAdapter(getActivity(), times, listClickListener, null);
        recyclerView.setAdapter(adapter);
    }

    private View.OnClickListener timesGravarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (timeNovo == null)
                timeNovo = new Time();

            timeNovo.setNome(binding.timesNome.getText().toString());

            if (timeNovo.getTimeID() == 0) {
                dbs.timeDAO().insert(timeNovo);
            } else {
                dbs.timeDAO().update(timeNovo);
            }
            long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);

            if (partidaID > 0) {

                Partida partida = dbs.partidaDAO().getPartida(partidaID);
                if (partida != null) {
                    if (timeNovo.getTimeID() == 1) {
                        partida.setNomeTime1(timeNovo.getNome());
                        dbs.partidaDAO().update(partida);
                    }else if (timeNovo.getTimeID() == 2) {
                        partida.setNomeTime2(timeNovo.getNome());
                        dbs.partidaDAO().update(partida);
                    }
                }
            }


            carregar();
            timeNovo = new Time();
            binding.timesNovoTime.setVisibility(View.GONE);

        }
    };
    private RecyclerViewListenerHack.OnClickListener listClickListener = new RecyclerViewListenerHack.OnClickListener<Time>() {
        @Override
        public void onClickListener(View view, int position, Time time) {


            timeNovo = time;
            binding.timesNovoTime.setVisibility(View.VISIBLE);
            binding.timesNome.setText(timeNovo.getNome());


        }
    };
}