package br.com.aj.truco.ui;

import android.app.Activity;
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
import br.com.aj.truco.adapter.JogadoresAdapter;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentJogadoresBinding;
import br.com.aj.truco.generic.RecyclerViewListenerHack;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class JogadoresFragment extends Fragment {

    private FragmentJogadoresBinding binding;
    private List<Jogador> jogadores;
    private List<Time> times;
    private Jogador jogadorNovo;

    private RecyclerView recyclerView;
    private Activity activity;
    private JogadoresAdapter adapter;
    private boolean ordenarJogadores = false;
    private AppRoomDatabase dbs;
    private int Ordem;
    private int Time;
    private long partidaID;
    private boolean desc;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();

        dbs = AppRoomDatabase.getDatabase(getContext());


        binding = FragmentJogadoresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.jogadoresRecycleview;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        carregar();

        binding.buttonReordenar.setOnClickListener(buttonReordenarClick);
        binding.buttonReordenarOk.setOnClickListener(buttonReordenarOkClick);
        binding.buttonReordenarCancelar.setOnClickListener(buttonReordenarCancelarClick);
        binding.buttonNovo.setOnClickListener(buttonNovoClick);
        binding.jogadoresGravar.setOnClickListener(jogadoresGravarClick);
        binding.jogadoresDeletar.setOnClickListener(jogadoresDeletarClick);
        binding.jogadoresCancelar.setOnClickListener(jogadoresCancelarClick);
        binding.jogadoresAtivoFiltro.setOnClickListener(jogadoresAtivoFiltroClick);

        partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;

    }


    public void carregar() {

        if (binding.jogadoresAtivoFiltro.isChecked())
            jogadores = dbs.jogadorDAO().getAllAtivosOrdem();
        else
            jogadores = dbs.jogadorDAO().getAllOrdem();

        adapter = new JogadoresAdapter(activity, jogadores, listClickListener, listLongClickListener);
        recyclerView.setAdapter(adapter);
        binding.jogadoresJogadores.setVisibility(View.VISIBLE);

    }

    public void deletar() {

        dbs.jogadorDAO().delete(jogadorNovo);
        dbs.partidaJogadorDAO().deleteByJogador(jogadorNovo.getJogadorID());
        dbs.partidaJogadaDAO().deleteByJogador(jogadorNovo.getJogadorID());

        carregar();
        jogadorNovo = new Jogador();
        binding.jogadoresNovoJogador.setVisibility(View.GONE);

        ordenarJogadores = false;

    }


    private View.OnClickListener buttonReordenarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (desc)
                times = dbs.timeDAO().getAtivos();
            else
                times = dbs.timeDAO().getAtivosAlt();

            desc = !desc;

            if (times.size() < 2) {
                new android.app.AlertDialog.Builder(getContext())
                        .setMessage("É necessátio que 2 time esteja ativo.")
                        .setCancelable(false)
                        .setPositiveButton("OK", null).show();
                return;
            } else if (times.size() > 2) {
                new android.app.AlertDialog.Builder(getContext())
                        .setMessage("Mais de 2 time está ativo.")
                        .setCancelable(false)
                        .setPositiveButton("OK", null).show();
                return;
            }

            Toast.makeText(getContext(), "Time 1 - " + times.get(0).getNome() + "\n\rTime 2 - " + times.get(1).getNome(), Toast.LENGTH_SHORT).show();

            ordenarJogadores = true;

            List<Jogador> TodosJogadores = dbs.jogadorDAO().getAll();
            for (Jogador jogador : TodosJogadores) {
                jogador.setOrdem(0);
                jogador.setTimeID(0);
                dbs.jogadorDAO().update(jogador);
            }

            binding.buttonReordenar.setVisibility(View.GONE);
            binding.buttonNovo.setVisibility(View.GONE);
            binding.buttonReordenarOk.setVisibility(View.VISIBLE);
            binding.buttonReordenarCancelar.setVisibility(View.VISIBLE);

            Ordem = 1;
            Time = 0;
            carregar();

        }
    };
    private View.OnClickListener buttonReordenarOkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            binding.buttonReordenar.setVisibility(View.VISIBLE);
            binding.buttonNovo.setVisibility(View.VISIBLE);
            binding.buttonReordenarOk.setVisibility(View.GONE);
            binding.buttonReordenarCancelar.setVisibility(View.GONE);

            carregar();

            ordenarJogadores = false;

        }
    };
    private View.OnClickListener buttonReordenarCancelarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            List<Jogador> TodosJogadores = dbs.jogadorDAO().getAll();
            for (Jogador jogador : TodosJogadores) {
                jogador.setOrdem(0);
                jogador.setTimeID(0);
                dbs.jogadorDAO().update(jogador);
            }

            binding.buttonReordenar.setVisibility(View.VISIBLE);
            binding.buttonNovo.setVisibility(View.VISIBLE);
            binding.buttonReordenarOk.setVisibility(View.GONE);
            binding.buttonReordenarCancelar.setVisibility(View.GONE);

            carregar();

            ordenarJogadores = false;


        }
    };


    private View.OnClickListener buttonNovoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            jogadorNovo = new Jogador();
            binding.jogadoresNome.setText("");
            binding.jogadoresNovoJogador.setVisibility(View.VISIBLE);
            binding.jogadoresDeletar.setVisibility(View.GONE);

            binding.jogadoresJogadores.setVisibility(View.GONE);

        }
    };
    private View.OnClickListener jogadoresGravarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (jogadorNovo == null)
                jogadorNovo = new Jogador();

            jogadorNovo.setNome(binding.jogadoresNome.getText().toString());
            jogadorNovo.setAtivo(binding.jogadoresAtivo.isChecked());

            if (jogadorNovo.getJogadorID() == 0) {
                dbs.jogadorDAO().insert(jogadorNovo);
            } else {
                dbs.jogadorDAO().update(jogadorNovo);
            }
            carregar();
            jogadorNovo = new Jogador();
            binding.jogadoresNovoJogador.setVisibility(View.GONE);
            ordenarJogadores = false;

        }
    };
    private View.OnClickListener jogadoresDeletarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (jogadorNovo != null) {

                long jogadas = dbs.partidaJogadaDAO().getCountByJogador(jogadorNovo.getJogadorID());


                new android.app.AlertDialog.Builder(getContext())
                        .setMessage(getString(jogadas > 0 ? R.string.msg_dialog_exclusao_jogador_registro : R.string.msg_dialog_exclusao_jogador))
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deletar();

                            }
                        }).show();


            } else {
                Toast.makeText(getContext(), "Erro o excluir jogador", Toast.LENGTH_LONG).show();
            }
        }
    };
    private View.OnClickListener jogadoresCancelarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            carregar();
            jogadorNovo = new Jogador();
            binding.jogadoresNovoJogador.setVisibility(View.GONE);
            ordenarJogadores = false;
        }
    };
    private View.OnClickListener jogadoresAtivoFiltroClick = v -> {
        carregar();
    };


    private RecyclerViewListenerHack.OnClickListener listClickListener = new RecyclerViewListenerHack.OnClickListener<Jogador>() {
        @Override
        public void onClickListener(View view, int position, Jogador jogador) {


            if (ordenarJogadores) {

                jogador.setOrdem(Ordem);
                jogador.setTimeID(times.get(Time).getTimeID());
                dbs.jogadorDAO().update(jogador);
                if (partidaID > 0) {
                    PartidaJogador partidaJogador = dbs.partidaJogadorDAO().getByJogadorPartida(jogador.getJogadorID(), partidaID);
                    if (partidaJogador != null) {
                        dbs.partidaJogadorDAO().update(partidaJogador);
                    } else {
                        partidaJogador = new PartidaJogador();
                        partidaJogador.setJogadorID(jogador.getJogadorID());
                        partidaJogador.setPartidaID(partidaID);
                        dbs.partidaJogadorDAO().insert(partidaJogador);
                    }
                }

                Ordem += 1;
                if (Time == 0)
                    Time = 1;
                else
                    Time = 0;

                adapter = new JogadoresAdapter(activity, jogadores, listClickListener, listLongClickListener);
                recyclerView.setAdapter(adapter);

            }
        }
    };
    private RecyclerViewListenerHack.OnLongClickListener listLongClickListener = new RecyclerViewListenerHack.OnLongClickListener<Jogador>() {
        @Override
        public void onLongPressClickListener(View view, int position, Jogador jogador) {

            jogadorNovo = jogador;
            binding.jogadoresNovoJogador.setVisibility(View.VISIBLE);
            binding.jogadoresNome.setText(jogadorNovo.getNome());
            binding.jogadoresAtivo.setChecked(jogadorNovo.isAtivo() == null ? false : jogadorNovo.isAtivo());
            binding.jogadoresDeletar.setVisibility(View.VISIBLE);
            binding.jogadoresJogadores.setVisibility(View.GONE);

        }
    };


}