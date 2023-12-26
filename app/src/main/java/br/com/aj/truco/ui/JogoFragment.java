package br.com.aj.truco.ui;

import static br.com.aj.truco.R.id;
import static br.com.aj.truco.R.layout;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;
import java.util.Objects;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.util.BaseFragment;
import br.com.aj.truco.util.Constantes;
import br.com.aj.truco.util.LogUtil;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class JogoFragment extends BaseFragment {

    private static final String TAG = JogoFragment.class.getSimpleName();


    //private JogoViewModel jogoViewModel;
    //private FragmentJogoBinding binding;


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

    private ImageButton buttonVitoriaTime1, buttonVitoriaTime2, buttonDesfazer;
    private Button buttonTrucar;
    private TextView textPlacar1, textPlacar2, textPartidas1, textPartidas2, textJogadorPe, textTime1Nome, textTime2Nome, textPontosPartida, textTimePe;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //jogoViewModel = new ViewModelProvider(this).get(JogoViewModel.class);

        final View root = inflater.inflate(layout.fragment_jogo, container, false);
        dbs = AppRoomDatabase.getDatabase(getContext());


        //binding = FragmentJogoinflate(inflater, container, false);
        //View root = getRoot();

        try {

            buttonVitoriaTime1 = root.findViewById(R.id.button_vitoria_time_1);
            buttonVitoriaTime2 = root.findViewById(R.id.button_vitoria_time_2);
            buttonTrucar = root.findViewById(R.id.button_trucar);
            buttonDesfazer = root.findViewById(R.id.button_desfazer);

            textPlacar1 = root.findViewById(R.id.text_placar_1);
            textPlacar2 = root.findViewById(R.id.text_placar_2);
            textPartidas1 = root.findViewById(R.id.text_partidas_1);
            textPartidas2 = root.findViewById(R.id.text_partidas_2);
            textJogadorPe = root.findViewById(R.id.text_jogador_pe);
            textTime1Nome = root.findViewById(R.id.text_time1_nome);
            textTime2Nome = root.findViewById(R.id.text_time2_nome);
            textPontosPartida = root.findViewById(R.id.text_pontosPartida);
            textTimePe = root.findViewById(R.id.text_time_pe);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }


        buttonVitoriaTime1.setOnClickListener(btnVitoriaTime1);
        buttonVitoriaTime2.setOnClickListener(btnVitoriaTime2);
        buttonTrucar.setOnClickListener(btnTruco);
        buttonDesfazer.setOnClickListener(btnDesfazer);

        textPlacar1.setOnLongClickListener(txtPlacar1LongClick);
        textPlacar2.setOnLongClickListener(txtPlacar2LongClick);
        textPartidas1.setOnLongClickListener(txtVitoria1LongClick);
        textPartidas2.setOnLongClickListener(txtVitoria2LongClick);

        textJogadorPe.setOnLongClickListener(textJogadorPeLongClick);
        //buttonNovaPartida.setOnClickListener(buttonNovaPartidaClick);

//        textTime1Nome.setText(partida.getNomeTime1());
//        textTime2Nome.setText(partida.getNomeTime2());
//        if (textTime1Nome.getTextColors().equals("")) {
//            Time time = dbs.timeDAO().getTime(1);
//            if (time != null)
//                textTime1Nome.setText(time.getNome());
//        }
//        if (textTime2Nome.getTextColors().equals("")) {
//            Time time = dbs.timeDAO().getTime(2);
//            if (time != null)
//                textTime2Nome.setText(time.getNome());
//        }


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

        if (partida != null) {
            textTime1Nome.setText(partida.getNomeTime1());
            textTime2Nome.setText(partida.getNomeTime2());
        }
        if (textTime1Nome.getTextColors().equals("")) {
            Time time = dbs.timeDAO().getTime(1);
            if (time != null)
                textTime1Nome.setText(time.getNome());
        }
        if (textTime2Nome.getTextColors().equals("")) {
            Time time = dbs.timeDAO().getTime(2);
            if (time != null)
                textTime2Nome.setText(time.getNome());
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
            partidaJogada.setTimeID(jogadorPe.getTimeID());
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
            partidaJogada.setTimeID(jogadorPe.getTimeID());
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
                buttonTrucar.setText("Seis");
            } else if (PontoPartida == 3) {
                PontoPartida = 6;
                buttonTrucar.setText("Nove");
            } else if (PontoPartida == 6) {
                PontoPartida = 9;
                buttonTrucar.setText("Doze");
            } else if (PontoPartida == 9) {
                PontoPartida = 12;
                buttonTrucar.setText("Voltar");
            } else if (PontoPartida == 12) {
                PontoPartida = 1;
                buttonTrucar.setText("Truco");
            }

//            buttonDesfazer.setVisibility(View.VISIBLE);
            textPontosPartida.setText(String.valueOf(PontoPartida));

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

            try {

                new android.app.AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.msg_dialog_desfazer))
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

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
                        }).show();


            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }


        }
    };

    private BroadcastReceiver mNovaPartidaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constantes.ACTION_NOVA_PARTIDA)) {

                try {

                    partidaID = SharedPreferencesUtil.getAppSharedPreferences(getContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);

                    QtdeJogadores = dbs.jogadorDAO().getMaxOrdem();
                    partida = dbs.partidaDAO().getPartida(partidaID);

//                jogadorPe = dbs.jogadorDAO().getJogador(partida.getJogadorID());
//                if (jogadorPe == null)
                    jogadorPe = dbs.jogadorDAO().getFirstJogador();

                    CarregarTela();
                } catch (Exception e) {
                    LogUtil.d(TAG, e.toString());
                }
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
                    textPlacar1.setText(String.valueOf(partida.getPontosTime1()));
                }
                if (time == 2) {
                    partida.setPontosTime2(numberPicker.getValue());
                    textPlacar2.setText(String.valueOf(partida.getPontosTime2()));
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
                    textPartidas1.setText(String.valueOf(partida.getVitoriaTime1()));
                }
                if (time == 2) {
                    partida.setVitoriaTime2(numberPicker.getValue());
                    textPartidas2.setText(String.valueOf(partida.getVitoriaTime2()));
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

        List<Jogador> jogadores = dbs.jogadorDAO().getJogadoresNaPartida();

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
//após trocar o pé, carrega os dados do novo pé selecionado
                    CarregarTela();
//                    //troca o nome na tela
//                    textJogadorPe.setText(jogadorPe.getNome());
//
//                    //troca o time do pé
//                    Time time = dbs.timeDAO().getTime(jogadorPe.getTimeID());
//                    if (time != null)
//                        textTimePe.setText(time.getNome());
//
//                    //troca o jogador ativo na partida atual
//                    if (partida != null)
//                        partida.setJogadorID(jogadorPe.getJogadorID());


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
            textJogadorPe.setText(jogadorPe.getNome());
            partidaJogador = dbs.partidaJogadorDAO().getByJogadorPartida(jogadorPe.getJogadorID(), partida.getPartidaID());

            Time time = dbs.timeDAO().getTime(jogadorPe.getTimeID());
            if (time != null)
                textTimePe.setText(time.getNome());
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
                //partidaJogador.setTimeJogadorID(jogadorPe.getTimeID());
                dbs.partidaJogadorDAO().insert(partidaJogador);
            }

            partida.setJogadorID(jogadorPe.getJogadorID());


            textJogadorPe.setText(jogadorPe.getNome());
            Time time = dbs.timeDAO().getTime(jogadorPe.getTimeID());
            if (time != null)
                textTimePe.setText(time.getNome());

            textPontosPartida.setText(String.valueOf(PontoPartida));

            textPlacar1.setText(Objects.toString(partida.getPontosTime1(), null));
            textPartidas1.setText(Objects.toString(partida.getVitoriaTime1(), null));
            textPlacar2.setText(Objects.toString(partida.getPontosTime2(), null));
            textPartidas2.setText(Objects.toString(partida.getVitoriaTime2(), null));
            textPlacar1.setText(Objects.toString(partida.getPontosTime1(), null));


            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.textColor, typedValue, true);
            int color = ContextCompat.getColor(getContext(), typedValue.resourceId);

            getContext().getTheme().resolveAttribute(R.attr.textColorNegativo, typedValue, true);
            int textColorNegativo = ContextCompat.getColor(getContext(), typedValue.resourceId);


            textPlacar1.setTextColor(color);
            textPlacar2.setTextColor(color);


            if (partida.getPontosTime1() == 11)
                textPlacar1.setTextColor(textColorNegativo);
            if (partida.getPontosTime2() == 11)
                textPlacar2.setTextColor(textColorNegativo);
        }
    }

    private void FinalizaRodada() {
        PontoPartida = 1;
        buttonTrucar.setText("Truco");

        dbs.partidaJogadorDAO().update(partidaJogador);

        FindNextJogador();
        CarregarTela();

        dbs.partidaDAO().update(partida);

    }

}