package br.com.aj.truco.ui.jogo;

import static br.com.aj.truco.R.id;
import static br.com.aj.truco.R.layout;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;
import java.util.Objects;

import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentJogoBinding;
import br.com.aj.truco.util.Constantes;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class JogoFragment extends Fragment {

    private JogoViewModel jogoViewModel;
    private FragmentJogoBinding binding;

    //private List<Time> times;
    //private List<Jogador> jogadores;
    private Jogador jogadorPe;
    //private List<Partida> partidas;
    private Partida partida;
    private int PontoPartida = 1;
    //private List<PartidaJogador> partidaJogadores;
    private PartidaJogador partidaJogador;
    //private UltimaPartida ultimaPartida;
    private int QtdeJogadores;
    private long partidaID;

    AppRoomDatabase dbs;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        jogoViewModel = new ViewModelProvider(this).get(JogoViewModel.class);

        dbs = AppRoomDatabase.getDatabase(getContext());


        binding = FragmentJogoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonVitoriaTime1.setOnClickListener(btnVitoriaTime1);
        binding.buttonVitoriaTime2.setOnClickListener(btnVitoriaTime2);
        binding.buttonTrucar.setOnClickListener(btnTruco);
        binding.buttonDesfazer.setOnClickListener(btnDesfazer);

        binding.textPlacar1.setOnLongClickListener(txtPlacar1LongClick);
        binding.textPlacar2.setOnLongClickListener(txtPlacar2LongClick);
        binding.textPartidas1.setOnLongClickListener(txtVitoria1LongClick);
        binding.textPartidas2.setOnLongClickListener(txtVitoria2LongClick);

        binding.textJogadorPe.setOnLongClickListener(textJogadorPeLongClick);
        //binding.buttonNovaPartida.setOnClickListener(buttonNovaPartidaClick);


        IntentFilter intentFilterNovaPartida = new IntentFilter(Constantes.ACTION_NOVA_PARTIDA);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mNovaPartidaReceiver, intentFilterNovaPartida);


        partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
        QtdeJogadores = dbs.jogadorDAO().getMaxOrdem();
        if (partida == null)
            partida = dbs.partidaDAO().getPartida(partidaID);
        if (jogadorPe == null) {

            if (partida != null && partida.getJogadorID() != 0)
                jogadorPe = dbs.jogadorDAO().getJogador(partida.getJogadorID());

            if (jogadorPe == null)
                jogadorPe = dbs.jogadorDAO().getFirstJogador();
        }

        if (jogadorPe == null || QtdeJogadores == 0)
            Toast.makeText(getActivity(), "Não há lista jogadores definida", Toast.LENGTH_LONG).show();

        CarregarTela();


        return root;
    }


    private View.OnClickListener btnVitoriaTime1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            PartidaJogada partidaJogada = new PartidaJogada();
            partidaJogada.setPartidaID(partida.getPartidaID());
            partidaJogada.setJogadorID(jogadorPe.getJogadorID());
            partidaJogada.setPontos(PontoPartida);
            partidaJogada.setVitoriasTime1(partida.getVitoriaTime1());
            partidaJogada.setVitoriasTime2(partida.getVitoriaTime2());
            partidaJogada.setPontosTime1(partida.getPontosTime1());
            partidaJogada.setPontosTime2(partida.getPontosTime2());

            if (jogadorPe.getTimeID() == 1) {
                partidaJogador.somarVitoria();
                partidaJogador.somarPontosGanhos(PontoPartida);
                partidaJogada.setVitoria(true);
            } else {
                partidaJogador.somarDerrota();
                partidaJogador.somarPontosPerdidos(PontoPartida);
                partidaJogada.setVitoria(false);
            }

            dbs.partidaJogadaDAO().insert(partidaJogada);


            partida.SomarPontoTime1(PontoPartida);


            FinalizaRodada();
        }
    };
    private View.OnClickListener btnVitoriaTime2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            PartidaJogada partidaJogada = new PartidaJogada();
            partidaJogada.setPartidaID(partida.getPartidaID());
            partidaJogada.setJogadorID(jogadorPe.getJogadorID());
            partidaJogada.setPontos(PontoPartida);
            partidaJogada.setVitoriasTime1(partida.getVitoriaTime1());
            partidaJogada.setVitoriasTime2(partida.getVitoriaTime2());
            partidaJogada.setPontosTime1(partida.getPontosTime1());
            partidaJogada.setPontosTime2(partida.getPontosTime2());

            if (jogadorPe.getTimeID() == 2) {
                partidaJogador.somarVitoria();
                partidaJogador.somarPontosGanhos(PontoPartida);
                partidaJogada.setVitoria(true);
            } else {
                partidaJogador.somarDerrota();
                partidaJogador.somarPontosPerdidos(PontoPartida);
                partidaJogada.setVitoria(false);
            }

            partida.SomarPontoTime2(PontoPartida);


            dbs.partidaJogadaDAO().insert(partidaJogada);


            FinalizaRodada();
        }
    };
    private View.OnClickListener btnTruco = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (PontoPartida == 1) {
                PontoPartida = 3;
                binding.buttonTrucar.setText("Seis");
            } else if (PontoPartida == 3) {
                PontoPartida = 6;
                binding.buttonTrucar.setText("Nove");
            } else if (PontoPartida == 6) {
                PontoPartida = 9;
                binding.buttonTrucar.setText("Doze");
            } else if (PontoPartida == 9) {
                PontoPartida = 12;
                binding.buttonTrucar.setText("Voltar");
            } else if (PontoPartida == 12) {
                PontoPartida = 1;
                binding.buttonTrucar.setText("Truco");
            }

//            binding.buttonDesfazer.setVisibility(View.VISIBLE);
            binding.textPontosPartida.setText(String.valueOf(PontoPartida));

        }
    };
    private View.OnLongClickListener txtPlacar1LongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            SelecionaPontos(1);
            return false;
        }
    };
    private View.OnLongClickListener txtPlacar2LongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            SelecionaPontos(2);
            return false;
        }
    };
    private View.OnLongClickListener txtVitoria1LongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            SelecionaPartidas(1);
            return false;
        }
    };
    private View.OnLongClickListener txtVitoria2LongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            SelecionaPartidas(2);
            return false;
        }
    };
    private View.OnLongClickListener textJogadorPeLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            SelecionarJogador();
            return false;
        }
    };
    private View.OnClickListener btnDesfazer = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //List<PartidaJogada> partidaJogadaList = dbs.partidaJogadaDAO().getAll();
            PartidaJogada partidaJogada = dbs.partidaJogadaDAO().getLast(partidaID);


            //desfaz o ponto
            if (partidaJogada != null && partidaJogada.getJogadorID() > 0) {
                partidaJogador = dbs.partidaJogadorDAO().getByJogadorPartida(partidaJogada.getJogadorID(), partida.getPartidaID());   //partidaJogadores.stream().filter(x -> x.getJogadorID() == partidaJogada.getJogadorID()).findFirst().orElse(null);


                if (partidaJogada.isVitoria()) {
                    partidaJogador.somarPontosGanhos(partidaJogada.getPontos() * -1);
                    partidaJogador.deduzirVitoria();
                } else {
                    partidaJogador.somarPontosPerdidos(partidaJogada.getPontos() * -1);
                    partidaJogador.deduzirDerrota();
                }

                partida.setPontosTime1(partidaJogada.getPontosTime1());
                partida.setPontosTime2(partidaJogada.getPontosTime2());
                partida.setVitoriaTime1(partidaJogada.getVitoriasTime1());
                partida.setVitoriaTime2(partidaJogada.getVitoriasTime2());

                //grava a partida e estatistica do jogador
                dbs.partidaJogadorDAO().update(partidaJogador);
                dbs.partidaDAO().update(partida);

                jogadorPe = dbs.jogadorDAO().getJogador(partidaJogada.getJogadorID());

                //deleta o historico
                dbs.partidaJogadaDAO().delete(partidaJogada);

                CarregarTela();


            } else {
                Toast.makeText(getContext(), "Não foi possível desfazer a jogada.", Toast.LENGTH_LONG).show();
            }
        }
    };

    private BroadcastReceiver mNovaPartidaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constantes.ACTION_NOVA_PARTIDA)) {

                partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);

                QtdeJogadores = dbs.jogadorDAO().getMaxOrdem();
                partida = dbs.partidaDAO().getPartida(partidaID);

//                jogadorPe = dbs.jogadorDAO().getJogador(partida.getJogadorID());
//                if (jogadorPe == null)
                jogadorPe = dbs.jogadorDAO().getFirstJogador();

                CarregarTela();
            }
        }
    };

    public void SelecionaPontos(int time) {

        final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(layout.number_picker_dialog, null);
        d.setTitle("Pontos do Time " + String.valueOf(time));
        d.setMessage("Selecione a pontuação");
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(id.dialog_number_picker);
        numberPicker.setMaxValue(11);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //Log.d(TAG, "onValueChange: ");
            }
        });
        d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Log.d(TAG, "onClick: " + numberPicker.getValue());
                if (time == 1) {
                    partida.setPontosTime1(numberPicker.getValue());
                    binding.textPlacar1.setText(String.valueOf(partida.getPontosTime1()));
                }
                if (time == 2) {
                    partida.setPontosTime2(numberPicker.getValue());
                    binding.textPlacar2.setText(String.valueOf(partida.getPontosTime2()));
                }
            }
        });
        d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();


    }

    public void SelecionaPartidas(int time) {

        final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(layout.number_picker_dialog, null);
        d.setTitle("Partidas do Time " + String.valueOf(time));
        d.setMessage("Selecione a pontuação");
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(id.dialog_number_picker);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //Log.d(TAG, "onValueChange: ");
            }
        });
        d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Log.d(TAG, "onClick: " + numberPicker.getValue());
                if (time == 1) {
                    partida.setVitoriaTime1(numberPicker.getValue());
                    binding.textPartidas1.setText(String.valueOf(partida.getVitoriaTime1()));
                }
                if (time == 2) {
                    partida.setVitoriaTime2(numberPicker.getValue());
                    binding.textPartidas2.setText(String.valueOf(partida.getVitoriaTime2()));
                }
            }
        });
        d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
    }

    private void SelecionarJogador() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Selecione o jogador");

        List<Jogador> jogadores = dbs.jogadorDAO().getJogadoresAtivos();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
        for (Jogador jogador : jogadores) {
            arrayAdapter.add(jogador.getNome());
        }


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                jogadorPe = jogadores.stream().filter(x -> x.getNome() == strName).findFirst().orElse(null);
                if (jogadorPe != null) {
                    binding.textJogadorPe.setText(jogadorPe.getNome());
                }
            }
        });
        builderSingle.show();
    }

    private void FindNextJogador() {

        int _ordem = jogadorPe.getOrdem() + 1;

        if (_ordem > QtdeJogadores)
            _ordem = 1;

        jogadorPe = dbs.jogadorDAO().getJogadorByOrdem(_ordem);

        if (jogadorPe != null) {
            binding.textJogadorPe.setText(jogadorPe.getNome());
            partidaJogador = dbs.partidaJogadorDAO().getByJogadorPartida(jogadorPe.getJogadorID(), partida.getPartidaID());
        }
    }

    private void CarregarTela() {

        if (jogadorPe != null && partida != null) {

            partidaJogador = dbs.partidaJogadorDAO().getByJogadorPartida(jogadorPe.getJogadorID(), partida.getPartidaID());

            //inclui se não achar
            if (partidaJogador == null) {
                partidaJogador = new PartidaJogador();
                partidaJogador.setJogadorID(jogadorPe.getJogadorID());
                partidaJogador.setPartidaID(partida.getPartidaID());
                partidaJogador.setTimeJogadorID(jogadorPe.getTimeID());
                dbs.partidaJogadorDAO().insert(partidaJogador);
            }

            partida.setJogadorID(jogadorPe.getJogadorID());


            binding.textJogadorPe.setText(jogadorPe.getNome());
            binding.textPontosPartida.setText(String.valueOf(PontoPartida));

            binding.textPlacar1.setText(Objects.toString(partida.getPontosTime1(), null));
            binding.textPartidas1.setText(Objects.toString(partida.getVitoriaTime1(), null));
            binding.textPlacar2.setText(Objects.toString(partida.getPontosTime2(), null));
            binding.textPartidas2.setText(Objects.toString(partida.getVitoriaTime2(), null));
            binding.textPlacar1.setText(Objects.toString(partida.getPontosTime1(), null));


            binding.textPlacar1.setTextColor(Color.WHITE);
            binding.textPlacar2.setTextColor(Color.WHITE);

            if (partida.getPontosTime1() == 11)
                binding.textPlacar1.setTextColor(Color.RED);
            if (partida.getPontosTime2() == 11)
                binding.textPlacar2.setTextColor(Color.RED);
        }
    }

    private void FinalizaRodada() {
        PontoPartida = 1;
        binding.buttonTrucar.setText("Truco");

        dbs.partidaJogadorDAO().update(partidaJogador);

        FindNextJogador();
        CarregarTela();

        dbs.partidaDAO().update(partida);

    }

}