package br.com.aj.truco.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.aj.truco.adapter.JogadoresAdapter;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentJogadoresBinding;
import br.com.aj.truco.generic.RecyclerViewListenerHack;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class JogadoresFragment extends Fragment {

    //private JogadoresViewModel jogadoresViewModel;
    private FragmentJogadoresBinding binding;
    private List<Jogador> jogadores;
    private Jogador jogadorNovo;

    private RecyclerView recyclerView;
    private Activity activity;
    private JogadoresAdapter adapter;
    private boolean ordenarJogadores = false;
    private AppRoomDatabase dbs;
    private int Ordem;
    private int Time;
    //private boolean alterar = false;
    private long partidaID;
    private Partida partida;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //jogadoresViewModel = new ViewModelProvider(this).get(JogadoresViewModel.class);

        activity = getActivity();

        dbs = AppRoomDatabase.getDatabase(getContext());


        binding = FragmentJogadoresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.jogadoresRecycleview;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));

        carregar();

        binding.buttonReordenar.setOnClickListener(buttonReordenarClick);
        binding.buttonReordenarOk.setOnClickListener(buttonReordenarOkClick);
        binding.buttonReordenarCancelar.setOnClickListener(buttonReordenarCancelarClick);
        binding.buttonNovo.setOnClickListener(buttonNovoClick);
        //binding.buttonAlterar.setOnClickListener(buttonAlterarClick);
        binding.jogadoresGravar.setOnClickListener(jogadoresGravarClick);
        binding.jogadoresDeletar.setOnClickListener(jogadoresDeletarClick);
        binding.jogadoresCancelar.setOnClickListener(jogadoresCancelarClick);
        binding.jogadoresAtivoFiltro.setOnClickListener(jogadoresAtivoFiltroClick);

        partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
        partida = dbs.partidaDAO().getPartida(partidaID);

//        jogadoresViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });
        return root;
    }

    public void carregar() {
        if (binding.jogadoresAtivoFiltro.isChecked())
            jogadores = dbs.jogadorDAO().getAllAtivosOrdem();
        else
            jogadores = dbs.jogadorDAO().getAllOrdem();
        //Ordenar();
        adapter = new JogadoresAdapter(activity, jogadores, listClickListener, null);
        recyclerView.setAdapter(adapter);
    }

    public void Ordenar() {

        Collections.sort(jogadores, new Comparator<Jogador>() {
            public int compare(Jogador obj1, Jogador obj2) {
                // ## Ascending order
                //return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
                return Integer.valueOf(obj1.getOrdem()).compareTo(Integer.valueOf(obj2.getOrdem())); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private View.OnClickListener buttonReordenarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


//            if ((partida.getPontosTime1() + partida.getPontosTime2() + partida.getVitoriaTime1() + partida.getVitoriaTime2()) > 0) {
//                Toast.makeText(getContext(), "Partida já iniciada, não é possível alterar os jogadores", Toast.LENGTH_LONG).show();
//            } else {


            ordenarJogadores = true;

            List<Jogador> TodosJogadores = dbs.jogadorDAO().getAll();
            for (Jogador jogador : TodosJogadores) {
                jogador.setOrdem(0);
                jogador.setTimeID(0);
                dbs.jogadorDAO().update(jogador);
            }

            //deleta a estatisticas dos jogadoresna partida corrente
            //dbs.partidaJogadorDAO().deleteByPartida(partidaID);

            binding.buttonReordenar.setVisibility(View.GONE);
            binding.buttonNovo.setVisibility(View.GONE);
            //binding.buttonAlterar.setVisibility(View.GONE);
            binding.buttonReordenarOk.setVisibility(View.VISIBLE);
            binding.buttonReordenarCancelar.setVisibility(View.VISIBLE);

            Ordem = 1;
            Time = 1;
            carregar();
//            adapter = new JogadoresAdapter(activity, jogadores, listClickListener, null);
//            recyclerView.setAdapter(adapter);
//            }
        }
    };
    private View.OnClickListener buttonReordenarOkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            binding.buttonReordenar.setVisibility(View.VISIBLE);
            binding.buttonNovo.setVisibility(View.VISIBLE);
            //binding.buttonAlterar.setVisibility(View.VISIBLE);

            binding.buttonReordenarOk.setVisibility(View.GONE);
            binding.buttonReordenarCancelar.setVisibility(View.GONE);
            carregar();

            ordenarJogadores = false;
            //alterar = false;

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
            //binding.buttonAlterar.setVisibility(View.VISIBLE);

            binding.buttonReordenarOk.setVisibility(View.GONE);
            binding.buttonReordenarCancelar.setVisibility(View.GONE);
            carregar();

            ordenarJogadores = false;
            //alterar = false;

        }
    };
    private View.OnClickListener buttonNovoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if ((partida.getPontosTime1() + partida.getPontosTime2() + partida.getVitoriaTime1() + partida.getVitoriaTime2()) > 0) {
//                Toast.makeText(getContext(), "Partida já iniciada, não é possível alterar os jogadores", Toast.LENGTH_LONG).show();
//            } else {
            jogadorNovo = new Jogador();
            binding.jogadoresNome.setText("");
            binding.jogadoresNovoJogador.setVisibility(View.VISIBLE);
            binding.jogadoresDeletar.setVisibility(View.GONE);
//            }
        }
    };
    //    private View.OnClickListener buttonAlterarClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
////            if ((partida.getPontosTime1() + partida.getPontosTime2() + partida.getVitoriaTime1() + partida.getVitoriaTime2()) > 0) {
////                Toast.makeText(getContext(), "Partida já iniciada, não é possível alterar os jogadores", Toast.LENGTH_LONG).show();
////            } else {
//            alterar = true;
//            Toast.makeText(getContext(), "Clique no jogador a ser alterado", Toast.LENGTH_LONG).show();
////            }
//        }
//    };
    private View.OnClickListener jogadoresGravarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (jogadorNovo == null)
                jogadorNovo = new Jogador();

            jogadorNovo.setNome(binding.jogadoresNome.getText().toString());
            jogadorNovo.setAtivo(binding.jogadoresAtivo.isChecked());

            if (jogadorNovo.getJogadorID() == 0) {
                dbs.jogadorDAO().insert(jogadorNovo);

                //só inclui a estatistica para jogadores ativos
                //long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
//                if (partidaID > 0) {
//                    PartidaJogador partidaJogador = new PartidaJogador();
//                    partidaJogador.setTimeJogadorID(jogadorNovo.getTimeID());
//                    partidaJogador.setJogadorID(jogadorNovo.getJogadorID());
//                    partidaJogador.setPartidaID(partidaID);
//                    dbs.partidaJogadorDAO().insert(partidaJogador);
//                }

            } else {
                dbs.jogadorDAO().update(jogadorNovo);
            }
            carregar();
            jogadorNovo = new Jogador();
            binding.jogadoresNovoJogador.setVisibility(View.GONE);
            //alterar = false;
            ordenarJogadores = false;

        }
    };
    private View.OnClickListener jogadoresDeletarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (jogadorNovo != null) {

                dbs.jogadorDAO().delete(jogadorNovo);
                dbs.partidaJogadorDAO().deleteByJogador(jogadorNovo.getJogadorID());
                dbs.partidaJogadaDAO().deleteByJogador(jogadorNovo.getJogadorID());

                carregar();
                jogadorNovo = new Jogador();
                binding.jogadoresNovoJogador.setVisibility(View.GONE);

                //alterar = false;
                ordenarJogadores = false;

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
            //alterar = false;
            ordenarJogadores = false;
        }
    };
    private View.OnClickListener jogadoresAtivoFiltroClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            carregar();
        }
    };
    private RecyclerViewListenerHack.OnClickListener listClickListener = new RecyclerViewListenerHack.OnClickListener<Jogador>() {
        @Override
        public void onClickListener(View view, int position, Jogador jogador) {
            if (ordenarJogadores) {

                jogador.setOrdem(Ordem);
                jogador.setTimeID(Time);
                dbs.jogadorDAO().update(jogador);
                //long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
                if (partidaID > 0) {
                    PartidaJogador partidaJogador = dbs.partidaJogadorDAO().getByJogadorPartida(jogador.getJogadorID(), partidaID);
                    if (partidaJogador != null) {
                        //partidaJogador.setTimeJogadorID(Time);
                        dbs.partidaJogadorDAO().update(partidaJogador);
                    } else {
                        //Toast.makeText(getContext(), "Partida do jogador não encontrada", Toast.LENGTH_LONG).show();
                        partidaJogador = new PartidaJogador();
                        //partidaJogador.setTimeJogadorID(jogador.getTimeID());
                        partidaJogador.setJogadorID(jogador.getJogadorID());
                        partidaJogador.setPartidaID(partidaID);
                        dbs.partidaJogadorDAO().insert(partidaJogador);
                    }
                }

                Ordem += 1;
                if (Time == 1)
                    Time = 2;
                else
                    Time = 1;

//                    if (Ordem > jogadores.stream().count())
//                        Ordenar();

                adapter = new JogadoresAdapter(activity, jogadores, listClickListener, null);
                recyclerView.setAdapter(adapter);

            } else {

                jogadorNovo = jogador;
                binding.jogadoresNovoJogador.setVisibility(View.VISIBLE);
                binding.jogadoresNome.setText(jogadorNovo.getNome());
                binding.jogadoresAtivo.setChecked(jogadorNovo.isAtivo() == null ? false : jogadorNovo.isAtivo());
                binding.jogadoresDeletar.setVisibility(View.VISIBLE);

            }
        }
    };
}