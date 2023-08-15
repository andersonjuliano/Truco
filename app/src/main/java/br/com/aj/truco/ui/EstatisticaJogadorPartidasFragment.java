package br.com.aj.truco.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.adapter.EstatisticasPorJogadorAdapter;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.dao.AppRoomDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstatisticaJogadorPartidasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstatisticaJogadorPartidasFragment extends Fragment {

    private ViewPager2 viewPager2;
    private AppRoomDatabase dbs;


    public static EstatisticaJogadorPartidasFragment newInstance(String param1, String param2) {
        EstatisticaJogadorPartidasFragment fragment = new EstatisticaJogadorPartidasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.fragment_estatistica_jogador_partidas, container, false);


        dbs = AppRoomDatabase.getDatabase(getContext());

        viewPager2 = rootView.findViewById(R.id.viewpager);


        List<Jogador>jogadorList = dbs.jogadorDAO().getAllAtivos();

        EstatisticasPorJogadorAdapter estatisticasPorJogadorAdapter = new EstatisticasPorJogadorAdapter(getActivity(), jogadorList, null, null);


        viewPager2.setAdapter(estatisticasPorJogadorAdapter);

        // To get swipe event of viewpager2
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // triggered when you select a new page
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });



        return rootView;
    }
}