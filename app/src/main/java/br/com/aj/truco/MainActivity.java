package br.com.aj.truco;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.databinding.ActivityMainBinding;
import br.com.aj.truco.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        CarregarIncial();
//        Bundle bundle = new Bundle();
//        bundle.putString("jogadores", String.valueOf(jogadores));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_jogadores, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private List<Time> times;
    private List<Jogador> jogadores;
    private Partida partida = new Partida();


    public void CarregarIncial() {

        if (times == null) {
            times = new ArrayList<>();

            Time time = new Time();
            time.setTimeID(1);
            time.setNome("Novos");
            times.add(time);

            time = new Time();
            time.setTimeID(2);
            time.setNome("Velhos");
            times.add(time);
        }

        if (jogadores == null) {

            jogadores = new ArrayList<>();

            Jogador jogador = new Jogador();
            jogador.setJogadorID(1);
            jogador.setOrdem(1);
            jogador.setNome("Juliano");
            jogadores.add(jogador);

            jogador = new Jogador();
            jogador.setJogadorID(2);
            jogador.setOrdem(2);
            jogador.setNome("Nelson");
            jogadores.add(jogador);


            jogador = new Jogador();
            jogador.setJogadorID(3);
            jogador.setOrdem(3);
            jogador.setNome("Eder");
            jogadores.add(jogador);

            jogador = new Jogador();
            jogador.setJogadorID(4);
            jogador.setOrdem(4);
            jogador.setNome("Genêsio");
            jogadores.add(jogador);

            jogador = new Jogador();
            jogador.setJogadorID(5);
            jogador.setOrdem(5);
            jogador.setNome("Gustavo");
            jogadores.add(jogador);

            jogador = new Jogador();
            jogador.setJogadorID(6);
            jogador.setOrdem(6);
            jogador.setNome("Zé");
            jogadores.add(jogador);
        }

    }
}