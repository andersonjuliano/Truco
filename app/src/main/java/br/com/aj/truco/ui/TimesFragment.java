package br.com.aj.truco.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.R;
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
    private Time timeNovo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTimesBinding.inflate(inflater, container, false);

        dbs = AppRoomDatabase.getDatabase(getContext());

        recyclerView = binding.timesRecycleview;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        binding.buttonNovo.setOnClickListener(novoClick);
        binding.timesGravar.setOnClickListener(timesGravarClick);
        binding.timesDeletar.setOnClickListener(timesDeletarClick);
        binding.timesCancelar.setOnClickListener(timessCancelarClick);

        carregar();

        return binding.getRoot();

    }


    private void carregar() {
        List<Time> objTimes = dbs.timeDAO().getAll();
        adapter = new TimesAdapter(getActivity(), objTimes, listClickListener, listLongClickListener);
        recyclerView.setAdapter(adapter);
    }

    private RecyclerViewListenerHack.OnClickListener listClickListener = new RecyclerViewListenerHack.OnClickListener<Time>() {
        @Override
        public void onClickListener(View view, int position, Time time) {

            int count = dbs.timeDAO().CountAtivos(time.getTimeID());
            if (count >= 2) {
                new android.app.AlertDialog.Builder(getContext())
                        .setMessage(getString(R.string.msg_dialog_times_ativos))
                        .setCancelable(false)
                        .setPositiveButton("OK", null).show();

            } else {
                time.setAtivo(!time.isAtivo());
                dbs.timeDAO().update(time);
                carregar();
            }

        }
    };
    private RecyclerViewListenerHack.OnLongClickListener listLongClickListener = new RecyclerViewListenerHack.OnLongClickListener<Time>() {
        @Override
        public void onLongPressClickListener(View view, int position, Time time) {
            timeNovo = time;
            binding.timesNovoTime.setVisibility(View.VISIBLE);
            binding.timesNome.setText(timeNovo.getNome());
        }
    };

    private View.OnClickListener novoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timeNovo = new Time();
            binding.timesNome.setText("");
            binding.timesNovoTime.setVisibility(View.VISIBLE);
            binding.timesDeletar.setVisibility(View.GONE);
        }
    };
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

//se tiver uma partida iniciada, atualiza o nome do time
            long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
            if (partidaID > 0) {
                Partida partida = dbs.partidaDAO().getPartida(partidaID);
                if (partida != null) {
                    new android.app.AlertDialog.Builder(getContext())
                            .setMessage(getString(R.string.msg_dialog_alterar_time))
                            .setCancelable(false)
                            .setNegativeButton("Não", null)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (timeNovo.getTimeID() == partida.getTime1ID()) {
                                        partida.setNomeTime1(timeNovo.getNome());
                                    } else if (timeNovo.getTimeID() == partida.getTime2ID()) {
                                        partida.setNomeTime2(timeNovo.getNome());
                                    }
                                    dbs.partidaDAO().update(partida);
                                }
                            }).show();
                }
            }

            carregar();
            binding.timesNovoTime.setVisibility(View.GONE);

        }
    };
    private View.OnClickListener timesDeletarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (timeNovo != null) {

                long jogadas = dbs.timeDAO().getCountByTime(timeNovo.getTimeID());

             new android.app.AlertDialog.Builder(getContext())
                        .setMessage(getString(jogadas > 0 ? R.string.msg_dialog_exclusao_time_registro : R.string.msg_dialog_exclusao_time))
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dbs.timeDAO().delete(timeNovo);

                                carregar();
                                binding.timesNovoTime.setVisibility(View.GONE);
                            }
                        }).show();


            } else {
                Toast.makeText(getContext(), "Erro o excluir time", Toast.LENGTH_LONG).show();
            }
        }
    };
    private View.OnClickListener timessCancelarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            carregar();
            binding.timesNovoTime.setVisibility(View.GONE);
        }
    };
}