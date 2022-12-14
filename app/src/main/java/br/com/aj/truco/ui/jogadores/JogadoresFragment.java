package br.com.aj.truco.ui.jogadores;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.adapter.JogadoresAdapter;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.MySingletonClass;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.databinding.FragmentJogadoresBinding;
import br.com.aj.truco.databinding.FragmentJogadoresBinding;
import br.com.aj.truco.generic.RecyclerViewListenerHack;

public class JogadoresFragment extends Fragment {

    private JogadoresViewModel jogadoresViewModel;
    private FragmentJogadoresBinding binding;
    private List<Jogador> jogadores;

    private RecyclerView recyclerView;
    private Activity activity;
    private JogadoresAdapter adapter;
    private boolean ordenarJogadores = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        jogadoresViewModel = new ViewModelProvider(this).get(JogadoresViewModel.class);

        activity = getActivity();

        binding = FragmentJogadoresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.jogadoresRecycleview;
        jogadores = MySingletonClass.getInstance().getJogadores();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));


        Ordenar();

        adapter = new JogadoresAdapter(activity, jogadores, listClickListener, null);
        recyclerView.setAdapter(adapter);

        binding.buttonReordenar.setOnClickListener(buttonReordenarClick);

        jogadoresViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    public  void Ordenar(){

        Collections.sort(jogadores, new Comparator<Jogador>(){
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

    private int Ordem;
    private View.OnClickListener buttonReordenarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ordenarJogadores = true;
            for (Jogador jogador : jogadores) {
                jogador.setOrdem(0);
                Ordem = 1;
                adapter = new JogadoresAdapter(activity, jogadores, listClickListener, null);
                recyclerView.setAdapter(adapter);
            }
        }
    };

    private RecyclerViewListenerHack.OnClickListener listClickListener = new RecyclerViewListenerHack.OnClickListener<Jogador>() {
        @Override
        public void onClickListener(View view, int position, Jogador object) {
            if (ordenarJogadores) {
                object.setOrdem(Ordem);
                Ordem += 1;

                if (Ordem > jogadores.stream().count() )
                    Ordenar();

                adapter = new JogadoresAdapter(activity, jogadores, listClickListener, null);
                recyclerView.setAdapter(adapter);
            }
        }
    };


}