package br.com.aj.truco.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Objects;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.MySingletonClass;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.classe.UltimaPartida;
import br.com.aj.truco.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private List<Time> times;
    private List<Jogador> jogadores;
    private Jogador jogadorPe;
    private List<Partida> partidas;
    private Partida partida = new Partida();
    private int PontoPartida = 1;
    private List<PartidaJogador> partidaJogadores;
    private PartidaJogador partidaJogador = new PartidaJogador();
    private UltimaPartida ultimaPartida;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
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


//try {
//         teste = getArguments().getString("jogadores");
//}
//catch (Exception ex){
//    int i1 = 1;
//}

        MySingletonClass.getInstance().CarregarIncial();
        times = MySingletonClass.getInstance().getTimes();
        jogadores = MySingletonClass.getInstance().getJogadores();
        jogadorPe = MySingletonClass.getInstance().getJogadorPe();
        partidas = MySingletonClass.getInstance().getPartidas();
        partida = MySingletonClass.getInstance().getPartida();
        partidaJogadores = MySingletonClass.getInstance().getPartidaJogadores();
        ultimaPartida = MySingletonClass.getInstance().getUltimaPartida();

        //CarregarIncial();
        if (jogadorPe == null)
            FindNextJogador();


        CarregarTela();

//               homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textViewPlacar1.setText(s);
//            }
//        });
        return root;
    }

    private View.OnClickListener btnVitoriaTime1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if(partida.getPontosTime2() == 11)
//                PontoPartida = 3;

            ultimaPartida.JogadorID = jogadorPe.getJogadorID();
            ultimaPartida.TimeID = jogadorPe.getTimeID();
            ultimaPartida.Pontos = PontoPartida;
            ultimaPartida.PontosTime1 = partida.getPontosTime1();
            ultimaPartida.PontosTime2 = partida.getPontosTime2();
    ultimaPartida.VitoriasTime1 = partida.getVitoriaTime1();
    ultimaPartida.VitoriasTime2 = partida.getVitoriaTime2();


            if (jogadorPe.getTimeID() == 1) {
                partidaJogador.somarVitoria();
                partidaJogador.somarPontosGanhos(PontoPartida);
                ultimaPartida.Vitoria = true;
            } else {
                partidaJogador.somarDerrota();
                partidaJogador.somarPontosPerdidos(PontoPartida);
                ultimaPartida.Vitoria = false;
            }

            partida.SomarPontoTime1(PontoPartida);
            FinalizaRodada();
        }
    };
    private View.OnClickListener btnVitoriaTime2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if(partida.getPontosTime1() == 11)
//                PontoPartida = 3;

            ultimaPartida.JogadorID = jogadorPe.getJogadorID();
            ultimaPartida.TimeID = jogadorPe.getTimeID();
            ultimaPartida.Pontos = PontoPartida;
            ultimaPartida.PontosTime1 = partida.getPontosTime1();
            ultimaPartida.PontosTime2 = partida.getPontosTime2();
            ultimaPartida.VitoriasTime1 = partida.getVitoriaTime1();
            ultimaPartida.VitoriasTime2 = partida.getVitoriaTime2();

            if (jogadorPe.getTimeID() == 2) {
                partidaJogador.somarVitoria();
                partidaJogador.somarPontosGanhos(PontoPartida);
                ultimaPartida.Vitoria = true;
            } else {
                partidaJogador.somarDerrota();
                partidaJogador.somarPontosPerdidos(PontoPartida);
                ultimaPartida.Vitoria = false;
            }

            partida.SomarPontoTime2(PontoPartida);
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

            //desfaz o ponto
            if (ultimaPartida != null && ultimaPartida.JogadorID > 0) {
                 partidaJogador = partidaJogadores.stream().filter(x -> x.getJogadorID() == ultimaPartida.JogadorID).findFirst().orElse(null);
                if (ultimaPartida.Vitoria) {
                    partidaJogador.somarPontosGanhos(ultimaPartida.Pontos * -1);
                    partidaJogador.deduzirVitoria();
                } else {
                    partidaJogador.somarPontosPerdidos(ultimaPartida.Pontos * -1);
                    partidaJogador.deduzirDerrota();
                }

               partida.setPontosTime1(ultimaPartida.PontosTime1);
               partida.setPontosTime2(ultimaPartida.PontosTime2);
               partida.setVitoriaTime1(ultimaPartida.VitoriasTime1);
               partida.setVitoriaTime2(ultimaPartida.VitoriasTime2);


//                if ((ultimaPartida.Vitoria && ultimaPartida.TimeID == 1) ||
//                        (!ultimaPartida.Vitoria && ultimaPartida.TimeID == 2)) {
//                    partida.SomarPontoTime1(ultimaPartida.Pontos * -1);
//                } else {
//                    partida.SomarPontoTime2(ultimaPartida.Pontos * -1);
//                }
                //volta o pé
                jogadorPe = jogadores.stream().filter(x -> x.getJogadorID() == ultimaPartida.JogadorID).findFirst().orElse(null);
                CarregarTela();
                ultimaPartida = new UltimaPartida();

                MySingletonClass.getInstance().setJogadorPe(jogadorPe);
                MySingletonClass.getInstance().setUltimaPartida(ultimaPartida);
                MySingletonClass.getInstance().setPartidaJogadores(partidaJogadores);
                MySingletonClass.getInstance().setPartida(partida);
                MySingletonClass.getInstance().setPartida(partida);
            }


        }
    };

    public void SelecionaPontos(int time) {

        final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        d.setTitle("Pontos do Time " + String.valueOf(time));
        d.setMessage("Selecione a pontuação");
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(11);
        numberPicker.setMinValue(1);
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
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        d.setTitle("Partidas do Time " + String.valueOf(time));
        d.setMessage("Selecione a pontuação");
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(11);
        numberPicker.setMinValue(1);
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
//                AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
//                builderInner.setMessage(strName);
//                builderInner.setTitle("Your Selected Item is");
//                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog,int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builderInner.show();
            }
        });
        builderSingle.show();
    }

    private void FindNextJogador() {

        int _ordem = 1;
        if (jogadorPe != null) {
            _ordem = jogadorPe.getOrdem() + 1;
        }
        if (_ordem > 6) {
            _ordem = 1;
        }

        int final_ordem = _ordem;
        jogadorPe = jogadores.stream().filter(x -> x.getOrdem() == final_ordem).findFirst().orElse(null);


        if (jogadorPe != null) {
            binding.textJogadorPe.setText(jogadorPe.getNome());

            MySingletonClass.getInstance().setJogadorPe(jogadorPe);

            partidaJogador = partidaJogadores.stream().filter(x -> x.getJogadorID() == jogadorPe.getJogadorID()).findFirst().orElse(null);
            if (partidaJogador == null) {
                //mensagem de erro???
                partidaJogador = new PartidaJogador();
            }
        }
    }

    private void CarregarTela() {
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

    private void FinalizaRodada() {
        PontoPartida = 1;
        binding.buttonTrucar.setText("Truco");

        MySingletonClass.getInstance().setPartidaJogadores(partidaJogadores);
        MySingletonClass.getInstance().setPartida(partida);

        FindNextJogador();
        CarregarTela();
    }


}