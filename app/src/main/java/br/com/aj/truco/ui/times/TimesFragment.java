package br.com.aj.truco.ui.times;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.adapter.JogadoresAdapter;
import br.com.aj.truco.adapter.TimesAdapter;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.FragmentJogadoresBinding;
import br.com.aj.truco.databinding.FragmentTimesBinding;



public class TimesFragment extends Fragment {

    private FragmentTimesBinding binding;
    private RecyclerView recyclerView;
    private TimesAdapter adapter;


    private AppRoomDatabase dbs;
    private List<Time> times;


    public TimesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTimesBinding.inflate(inflater, container, false);

        dbs = AppRoomDatabase.getDatabase(getContext());

        recyclerView = binding.timesRecycleview;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        times = dbs.timeDAO().getAll();
        //Ordenar();
        adapter = new TimesAdapter(getActivity(), times, null, null);
        recyclerView.setAdapter(adapter);





        return  binding.getRoot();

    }
}