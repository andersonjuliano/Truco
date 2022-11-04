package br.com.aj.truco;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.ActivityMainBinding;
import br.com.aj.truco.util.Constantes;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private final String TAG = "TAG_TRUCO";

    private MenuItem mNovaPartida;
    private AppRoomDatabase dbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbs = AppRoomDatabase.getDatabase(getBaseContext());

        //CarregarIncial();
//        Bundle bundle = new Bundle();
//        bundle.putString("jogadores", String.valueOf(jogadores));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain. fab.setOnClickListener(new View.OnClickListener() {
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
                R.id.nav_jogar, R.id.nav_jogadores, R.id.nav_estatistica, R.id.nav_times)
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
        mNovaPartida = menu.findItem(R.id.action_nova_partida);
        mNovaPartida.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {

                    List<Partida> partidaList = dbs.partidaDAO().getAll();


                    if (dbs.timeDAO().CountAll() ==0) {

                       Time time = new Time();
                        time.setNome("Novos");
                        dbs.timeDAO().insert(time);

                       time = new Time();
                        time.setNome("Velhos");
                        dbs.timeDAO().insert(time);
                    }

                    Calendar c = Calendar.getInstance();

                    Partida partida = new Partida();
                    partida.setDataPartida(c.getTime().getTime());
                    long id = dbs.partidaDAO().insert(partida);


                    //ao iniciar uma nova partida, remove o histórico
                    dbs.partidaJogadaDAO().deleteAll();
                    

                    SharedPreferences.Editor editor = SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).edit();
                    editor.putLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, id);
                    editor.commit();

                    carregarPartidaJogador(id);

                    Toast.makeText(getBaseContext(), "Nova partida iniciada", Toast.LENGTH_LONG).show();

                    Intent receiverIntent = new Intent(Constantes.ACTION_NOVA_PARTIDA);
                    //receiverIntent.putExtra(MensagemChat.EXTRA_KEY, objMensagemChat);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(receiverIntent);


                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                return true;
            }
        });

        return true;
    }

    private final Calendar dataSelecionada = Calendar.getInstance();

    public void SelecionaData() {

        final AlertDialog.Builder d = new AlertDialog.Builder(getBaseContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.date_picker_dialog, null);
        d.setTitle("Selecione a data");
        d.setMessage("Selecione a data");
        d.setView(dialogView);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

//        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
//                //Log.d(TAG, "onValueChange: ");
//            }
//        });
        d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Log.d(TAG, "onClick: " + numberPicker.getValue());

                dataSelecionada.set(Calendar.DATE, datePicker.getDayOfMonth());
                dataSelecionada.set(Calendar.MONTH, datePicker.getMonth());
                dataSelecionada.set(Calendar.YEAR, datePicker.getYear());


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

    private List<Jogador> cargaInicialJogadores() {


        Jogador jogador = new Jogador();
        jogador.setOrdem(1);
        jogador.setTimeID(1);
        jogador.setNome("Juliano");
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(2);
        jogador.setTimeID(2);
        jogador.setNome("Nelson");
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(3);
        jogador.setTimeID(1);
        jogador.setNome("Eder");
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(4);
        jogador.setTimeID(2);
        jogador.setNome("Genêsio");
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(5);
        jogador.setTimeID(1);
        jogador.setNome("Gustavo");
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(6);
        jogador.setTimeID(2);
        jogador.setNome("Zé");
        dbs.jogadorDAO().insert(jogador);


        return dbs.jogadorDAO().getAll();

    }

    private void carregarPartidaJogador(long partidaID) {

        List<Jogador> jogadorList = dbs.jogadorDAO().getAll();
        if (jogadorList == null || jogadorList.stream().count() == 0)
            jogadorList = cargaInicialJogadores();

//        SharedPreferences sharedPreferences = SharedPreferencesUtil.getAppSharedPreferences(getBaseContext());
        //int partidaID = sharedPreferences.getInt(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);


        for (Jogador jogador : jogadorList) {

            PartidaJogador partidaJogador = new PartidaJogador();
            partidaJogador.setTimeJogadorID(jogador.getTimeID());
            partidaJogador.setJogadorID(jogador.getJogadorID());
            partidaJogador.setPartidaID(partidaID);
            dbs.partidaJogadorDAO().insert(partidaJogador);



        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }





//    private List<Time> times;
//    private List<Jogador> jogadores;
//    private Partida partida = new Partida();
//
//
//    public void CarregarIncial() {
//
//        if (times == null) {
//            times = new ArrayList<>();
//
//            Time time = new Time();
//            time.setTimeID(1);
//            time.setNome("Novos");
//            times.add(time);
//
//            time = new Time();
//            time.setTimeID(2);
//            time.setNome("Velhos");
//            times.add(time);
//        }
//
//        if (jogadores == null) {
//
//            jogadores = new ArrayList<>();
//
//            Jogador jogador = new Jogador();
//            jogador.setJogadorID(1);
//            jogador.setOrdem(1);
//            jogador.setNome("Juliano");
//            jogadores.add(jogador);
//
//            jogador = new Jogador();
//            jogador.setJogadorID(2);
//            jogador.setOrdem(2);
//            jogador.setNome("Nelson");
//            jogadores.add(jogador);
//
//
//            jogador = new Jogador();
//            jogador.setJogadorID(3);
//            jogador.setOrdem(3);
//            jogador.setNome("Eder");
//            jogadores.add(jogador);
//
//            jogador = new Jogador();
//            jogador.setJogadorID(4);
//            jogador.setOrdem(4);
//            jogador.setNome("Genêsio");
//            jogadores.add(jogador);
//
//            jogador = new Jogador();
//            jogador.setJogadorID(5);
//            jogador.setOrdem(5);
//            jogador.setNome("Gustavo");
//            jogadores.add(jogador);
//
//            jogador = new Jogador();
//            jogador.setJogadorID(6);
//            jogador.setOrdem(6);
//            jogador.setNome("Zé");
//            jogadores.add(jogador);
//        }
//
//    }

}