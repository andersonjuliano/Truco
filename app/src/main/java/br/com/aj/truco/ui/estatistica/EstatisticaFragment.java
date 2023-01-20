package br.com.aj.truco.ui.estatistica;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.aj.truco.adapter.PartidaJogadoresAdapter;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.PartidaSelecao;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentEstatisticaBinding;
import br.com.aj.truco.generic.RecyclerViewListenerHack;
import br.com.aj.truco.ui.EstatisticaJogadorGraficoActivity;
import br.com.aj.truco.ui.PartidaPontosActivity;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class EstatisticaFragment extends Fragment {

    private EstatisticaViewModel estatisticaViewModel;
    private FragmentEstatisticaBinding binding;

    private RecyclerView recyclerView;
    private Activity activity;
    private PartidaJogadoresAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        estatisticaViewModel =
                new ViewModelProvider(this).get(EstatisticaViewModel.class);

        binding = FragmentEstatisticaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = getActivity();

        recyclerView = binding.estatisticaRecycleview;

        AppRoomDatabase dbs = AppRoomDatabase.getDatabase(getContext());
        long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);

        Partida partida = dbs.partidaDAO().getPartida(partidaID);
        if (partida != null) {
            binding.estatisticaPartidaText.setText("Partida: " + partida.getTitulo());
        }


        List<PartidaJogador> partidaJogadores = dbs.partidaJogadorDAO().getByPartida(partidaID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new PartidaJogadoresAdapter(activity, partidaJogadores, listClickListener, null);
        recyclerView.setAdapter(adapter);

        binding.estatisticaPartidaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Selecione a partida");

                List<Partida> partidaList = dbs.partidaDAO().getAll();
                final ArrayAdapter<PartidaSelecao> arrayPartidasSelecionadas = new ArrayAdapter<PartidaSelecao>(getContext(), android.R.layout.select_dialog_multichoice);
                final boolean[] arraySelecionadas = new boolean[partidaList.size() + 1];
                final String[] arrayPartidas = new String[partidaList.size() + 1];


                arrayPartidasSelecionadas.add(new PartidaSelecao(0, "Todas as Partidas", false));
                arraySelecionadas[0] = false;
                arrayPartidas[0] = "Todas as Partidas";

                int i = 1;

                for (Partida partida1 : partidaList) {
                    arraySelecionadas[i] = false;
                    arrayPartidas[i] = partida1.getTitulo();
                    arrayPartidasSelecionadas.add(new PartidaSelecao(partida1.getPartidaID(), partida1.getTitulo(), false));
                    i++;
                }


                builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        List<Long> partidas = new ArrayList<>();
                        List<PartidaJogador> partidaJogadores = new ArrayList<>();
                        boolean todos = false;
                        String titulo = "";
                        for (int i = 0; i < arrayPartidasSelecionadas.getCount(); i++) {
                            if (arrayPartidasSelecionadas.getItem(i).Selecionada) {
                                if (arrayPartidasSelecionadas.getItem(i).PartidaID == 0) {
                                    partidaJogadores = dbs.partidaJogadorDAO().getAllConsolidado();
                                    todos = true;
                                    titulo = "Todas as partidas";
                                    break;
                                } else {
                                    partidas.add(arrayPartidasSelecionadas.getItem(i).PartidaID);
                                    titulo = "Partida: " + arrayPartidasSelecionadas.getItem(i).Nome;
                                }
                            }
                        }
                        if (!todos) {
                            if (partidas.stream().count() == 1) {
                                partidaJogadores = dbs.partidaJogadorDAO().getByPartida(partidas.get(0));
                            } else if (partidas.stream().count() > 0) {
                                partidaJogadores = dbs.partidaJogadorDAO().getByPartidas(partidas);
                                if (partidas.stream().count() > 1) {
                                    titulo = "Partidas Selecionadas";
                                }
                            } else {
                                long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
                                Partida partida = dbs.partidaDAO().getPartida(partidaID);
                                if (partida != null)
                                    titulo = "Partida: " + partida.getTitulo();

                                partidaJogadores = dbs.partidaJogadorDAO().getByPartida(partidaID);
                            }
                        }

                        //somente carrega a tela se tiver registros
                        if (partidaJogadores != null && partidaJogadores.stream().count() > 0) {
                            binding.estatisticaPartidaText.setText(titulo);
                            adapter = new PartidaJogadoresAdapter(activity, partidaJogadores, listClickListener, null);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });

                builderSingle.setMultiChoiceItems(arrayPartidas, arraySelecionadas, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        arrayPartidasSelecionadas.getItem(which).Selecionada = isChecked;
                        //Toast.makeText(getContext(), arrayPartidasSelecionadas.getItem(which).Nome + " " + isChecked, Toast.LENGTH_SHORT).show();
                    }
                });


//                builderSingle.setAdapter(arrayPartidas, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        long partidaID = Long.parseLong(arrayPartidas.getItem(which).split("-")[0].trim());
//                        List<PartidaJogador> partidaJogadores;
//                        if (partidaID > 0) {
//                            partidaJogadores = dbs.partidaJogadorDAO().getByPartida(partidaID);
//                        } else {
//                            partidaJogadores = dbs.partidaJogadorDAO().getAllConsolidado();
//                        }
//
//                        adapter = new PartidaJogadoresAdapter(activity, partidaJogadores, null, null);
//                        recyclerView.setAdapter(adapter);
//
//                    }
//                });
                builderSingle.show();
            }
        });

//        final TextView textView = binding.textSlideshow;
        estatisticaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

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

            if (object instanceof PartidaJogador) {

//               Intent intent = new Intent(getActivity(), EstatisticaJogadorGraficoActivity.class);
//               intent.putExtra(Jogador.EXTRA_KEY,  ((PartidaJogador) object).getJogadorID());
//                startActivity(intent);

                Intent intent = new Intent(getActivity(), PartidaPontosActivity.class);
                intent.putExtra(Partida.EXTRA_KEY, ((PartidaJogador) object).getPartidaID());
                intent.putExtra(Jogador.EXTRA_KEY, ((PartidaJogador) object).getJogadorID());
                startActivity(intent);

            }
        }
    };


}
