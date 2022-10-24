package br.com.aj.truco.ui.estatistica;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.adapter.PartidaJogadoresAdapter;
import br.com.aj.truco.classe.MySingletonClass;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.databinding.FragmentEstatisticaBinding;

public class EstatisticaFragment extends Fragment {

    private EstatisticaViewModel estatisticaViewModel;
    private FragmentEstatisticaBinding binding;

    private RecyclerView recyclerView;
    private Activity activity;
    private PartidaJogadoresAdapter adapter;
    private List<PartidaJogador> partidaJogadores;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        estatisticaViewModel =
                new ViewModelProvider(this).get(EstatisticaViewModel.class);

        binding = FragmentEstatisticaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = getActivity();

        recyclerView = binding.partidaJogadoresRecycleview;
        partidaJogadores = MySingletonClass.getInstance().getPartidaJogadores();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));

        adapter = new PartidaJogadoresAdapter(activity, partidaJogadores, null, null);
        recyclerView.setAdapter(adapter);

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
}