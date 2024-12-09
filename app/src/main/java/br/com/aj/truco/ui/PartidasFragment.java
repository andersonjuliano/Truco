package br.com.aj.truco.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.adapter.PartidasAdapter;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaPesquisa;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentPartidasBinding;
import br.com.aj.truco.generic.RecyclerViewListenerHack;
import br.com.aj.truco.util.Constantes;
import br.com.aj.truco.util.SharedPreferencesUtil;


public class PartidasFragment extends Fragment {


    private FragmentPartidasBinding binding;

    private RecyclerView recyclerView;
    private Activity activity;
    private PartidasAdapter adapter;
    private AppRoomDatabase dbs;

    public PartidasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPartidasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = getActivity();

        recyclerView = binding.partidasRecycleview;


        dbs = AppRoomDatabase.getDatabase(getContext());

        List<PartidaPesquisa> partidaList = dbs.partidaDAO().getAllP();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new PartidasAdapter(activity, partidaList, listClickListener, listLongClickListener);
        recyclerView.setAdapter(adapter);


        binding.partidasButtomTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<PartidaPesquisa> partidaList = dbs.partidaDAO().getTotalPartidas();

                adapter = new PartidasAdapter(activity, partidaList, null, null);
                recyclerView.setAdapter(adapter);

            }
        });

        binding.partidasButtomTodas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<PartidaPesquisa> partidaList = dbs.partidaDAO().getAllP();

                adapter = new PartidasAdapter(activity, partidaList, listClickListener, listLongClickListener);
                recyclerView.setAdapter(adapter);

            }
        });

        binding.partidasButtomUltimas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
                d.setTitle("Selecione a quatidade de partidas");
                d.setView(dialogView);
                final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
                numberPicker.setMaxValue(99);
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

                        List<PartidaPesquisa> partidaList = dbs.partidaDAO().getLastPartidas(numberPicker.getValue());

                        adapter = new PartidasAdapter(activity, partidaList, null, null);
                        recyclerView.setAdapter(adapter);
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
            if (object instanceof Partida && ((Partida) object).getPartidaID() != 0) {



                Partida partida = (Partida) object;

                if (partida.getTime1ID() == 0 || partida.getTime2ID() == 0)
                {
                    partida.setTime1ID(1);
                    partida.setTime2ID(2);
                    dbs.partidaDAO().update(partida);
                }

                long id = ((Partida) object).getPartidaID();
                SharedPreferences.Editor editor = SharedPreferencesUtil.getAppSharedPreferences(getContext()).edit();
                editor.putLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, id);
                editor.commit();


                Toast.makeText(getContext(), "Partida carregada", Toast.LENGTH_LONG).show();

                Intent receiverIntent = new Intent(Constantes.ACTION_NOVA_PARTIDA);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(receiverIntent);


                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_jogar);

            }
        }
    };
    private RecyclerViewListenerHack.OnLongClickListener listLongClickListener = new RecyclerViewListenerHack.OnLongClickListener() {
        @Override
        public void onLongPressClickListener(View view, int position, Object object) {

            if (object instanceof Partida && ((Partida) object).getPartidaID() != 0) {



                Intent intent = new Intent(getActivity(), PartidaPontosActivity.class);
                intent.putExtra(Partida.EXTRA_KEY, ((Partida) object).getPartidaID());
                startActivity(intent);


            }

        }
    };


}