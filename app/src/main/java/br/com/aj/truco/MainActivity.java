package br.com.aj.truco;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.List;

import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.databinding.ActivityMainBinding;
import br.com.aj.truco.ui.SettingsActivity;
import br.com.aj.truco.util.Constantes;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private final String TAG = "TAG_TRUCO";


    private AppRoomDatabase dbs;

    //private final RoomBackup roomBackup = new RoomBackup(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbs = AppRoomDatabase.getDatabase(getBaseContext());

        if (SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).getBoolean(SharedPreferencesUtil.KEY_TEMA_ESCURO, false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        CarregarIncial();

        br.com.aj.truco.databinding.ActivityMainBinding objBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(objBinding.getRoot());

        setSupportActionBar(objBinding.appBarMain.toolbar);
        DrawerLayout drawer = objBinding.drawerLayout;
        NavigationView navigationView = objBinding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_jogar, R.id.nav_times, R.id.nav_jogadores, R.id.nav_estatistica,
                R.id.nav_partidas, R.id.nav_estatistica_jogadores, R.id.nav_estatistica_jogador_partidas,
                R.id.nav_historico_partida)
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

        MenuItem mNovaPartida = menu.findItem(R.id.action_nova_partida);
        mNovaPartida.setOnMenuItemClickListener(item -> {
            try {

                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setMessage(getString(R.string.action_nova_partida_confirmar))
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("OK", (dialogInterface, i) -> {

                            // dbs = AppRoomDatabase.getDatabase(getBaseContext());
                            Time objTime1 = dbs.timeDAO().getTime(1);
                            Time objTime2 = dbs.timeDAO().getTime(2);

                            Calendar c = Calendar.getInstance();

                            Partida partida = new Partida();
                            partida.setDataPartida(c.getTime().getTime());

                            partida.setTime1ID(objTime1.getTimeID());
                            partida.setNomeTime1(objTime1.getNome());

                            partida.setTime2ID(objTime2.getTimeID());
                            partida.setNomeTime2(objTime2.getNome());

                            long id = dbs.partidaDAO().insert(partida);

                            SharedPreferences.Editor editor = SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).edit();
                            editor.putLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, id);
                            editor.apply();
                            editor.commit();

                            carregarPartidaJogador(id);
                            Toast.makeText(getBaseContext(), "Nova partida iniciada", Toast.LENGTH_LONG).show();

                            Intent receiverIntent = new Intent(Constantes.ACTION_NOVA_PARTIDA);
                            //receiverIntent.putExtra(MensagemChat.EXTRA_KEY, objMensagemChat);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(receiverIntent);


                        }).show();


            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return true;
        });


        MenuItem mLimparHistorico = menu.findItem(R.id.action_limpar_hitorico);
        mLimparHistorico.setOnMenuItemClickListener(item -> {

            try {

                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setMessage(getString(R.string.action_limpar_hitorico_confirmar))
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("OK", (dialogInterface, i) -> Toast.makeText(getBaseContext(), "Limpeza ainda não implementada", Toast.LENGTH_LONG).show()).show();


            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return false;
        });


        MenuItem mTemaEscuro = menu.findItem(R.id.action_tema_escuro);
        if (SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).getBoolean(SharedPreferencesUtil.KEY_TEMA_ESCURO, false)) {
            mTemaEscuro.setTitle(R.string.action_tema_claro);
        } else {
            mTemaEscuro.setTitle(R.string.action_tema_escuro);
        }
        mTemaEscuro.setOnMenuItemClickListener(item -> {
            try {


                boolean bTemaEscuro = !SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).getBoolean(SharedPreferencesUtil.KEY_TEMA_ESCURO, false);
                SharedPreferences.Editor editor = SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).edit();
                editor.putBoolean(SharedPreferencesUtil.KEY_TEMA_ESCURO, bTemaEscuro);
                editor.apply();
                editor.commit();

                if (bTemaEscuro) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mTemaEscuro.setTitle(R.string.action_tema_claro);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mTemaEscuro.setTitle(R.string.action_tema_escuro);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return true;
        });


        MenuItem mLimparEstatitica = menu.findItem(R.id.action_limpar_estatistica);
        mLimparEstatitica.setOnMenuItemClickListener(item -> {

            try {

                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setMessage(getString(R.string.action_limpar_estatisticas_confirmar))
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("OK", (dialogInterface, i) -> {

                            long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
                            int qtde = dbs.partidaJogadorDAO().deleteJogadorExcluido();
                            qtde += dbs.partidaJogadorDAO().deleteNull(partidaID);
                            qtde += dbs.partidaJogadorDAO().deleteZero();
                            qtde += dbs.partidaDAO().deleteNull(partidaID);
                            Toast.makeText(getBaseContext(), qtde + " estatísticas excluídas", Toast.LENGTH_LONG).show();

                        }).show();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return false;
        });

        MenuItem mCorrigirEstatitica = menu.findItem(R.id.action_corrigir_estatistica);
        mCorrigirEstatitica.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                try {

                    new android.app.AlertDialog.Builder(MainActivity.this)
                            .setMessage(getString(R.string.action_corrigir_estatisticas_confirmar))
                            .setCancelable(false)
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
//                            .setNeutralButton("Corrigir", new DialogInterface.OnClickListener() {
//                                //trocar os jogadores de time, e corrigir as estatisticas
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    //corrige a situação que os times estavam corretos, porém marquei os pontos invertidos
//                                    //tipo eu ganhei, mas marquei vitória dos velhos
//                                    long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
//                                    List<PartidaJogada> partidaJogadaList = dbs.partidaJogadaDAO().getByPartida(partidaID);
//                                    for (PartidaJogada partidaJogada : partidaJogadaList) {
//                                        int PontosTime1 = partidaJogada.getPontosTime1();
//                                        int PontosTime2 = partidaJogada.getPontosTime2();
//                                        int VitoriasTime1 = partidaJogada.getVitoriasTime1();
//                                        int VitoriasTime2 = partidaJogada.getVitoriasTime2();
//
//                                        //inverte os pontos e a vitória e corrige a partidaID
//                                        partidaJogada.setVitoria(!partidaJogada.isVitoria());
//                                        partidaJogada.setPontosTime1(PontosTime2);
//                                        partidaJogada.setPontosTime2(PontosTime1);
//                                        partidaJogada.setVitoriasTime1(VitoriasTime2);
//                                        partidaJogada.setVitoriasTime2(VitoriasTime1);
//                                        //salva as alterações
//                                        dbs.partidaJogadaDAO().update(partidaJogada);
//                                    }
//
//
////                                    long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);
////                                    List<PartidaJogador> partidaJogadorList = dbs.partidaJogadorDAO().getByPartida(partidaID);
////                                    for (PartidaJogador partidaJogador : partidaJogadorList) {
////
////                                        //corrige o time conforme está no cadastro
////
////                                        Jogador objJogador = dbs.jogadorDAO().getJogador(partidaJogador.getJogadorID());
////
////                                        partidaJogador.setTimeJogadorID(objJogador.getTimeID());
////
////
////                                        //pega todas as partidas jogadas daquele jogador
////                                        List<PartidaJogada> partidaJogadaList = dbs.partidaJogadaDAO().getByPartidaJogador(partidaJogador.getPartidaID(), partidaJogador.getJogadorID());
////                                        for (PartidaJogada partidaJogada : partidaJogadaList) {
////                                            //corrige o time
////                                            partidaJogada.setTimeID(partidaJogador.getTimeJogadorID());
////
////                                            //inverte a vitória/derrota
////                                            partidaJogada.setVitoria(!partidaJogada.isVitoria());
////
////                                            //salva as alterações
////                                            dbs.partidaJogadaDAO().update(partidaJogada);
////                                        }
////
////                                        //faz o update
////                                        dbs.partidaJogadorDAO().update(partidaJogador);
////                                    }
//                                }
//                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    long partidaID = SharedPreferencesUtil.getAppSharedPreferences(getBaseContext()).getLong(SharedPreferencesUtil.KEY_PARTIDAID_ATIVA, 0);

                                    //List<PartidaJogador> partidaJogadorList = dbs.partidaJogadorDAO().getByPartida(partidaID);
                                    List<Jogador> jogadorList = dbs.jogadorDAO().getAll();

                                    int qtde = 0;
                                    for (Jogador jogador : jogadorList) {

                                        boolean incluir = false;

                                        PartidaJogador partidaJogador = dbs.partidaJogadorDAO().getByJogadorPartida(jogador.getJogadorID(), partidaID);
                                        if (partidaJogador == null) {
                                            partidaJogador = new PartidaJogador();
                                            partidaJogador.setJogadorID(jogador.getJogadorID());
                                            partidaJogador.setPartidaID(partidaID);
                                            incluir = true;
                                        }

                                        PartidaJogador partidaJogadorNova = dbs.partidaJogadorDAO().getFromPartidaJogadaByPartidaJogador(partidaID, partidaJogador.getJogadorID());
                                        if (partidaJogadorNova != null) {

                                            partidaJogador.setVitoria(partidaJogadorNova.getVitoria());
                                            partidaJogador.setDerrota(partidaJogadorNova.getDerrota());
                                            partidaJogador.setPontosPerdidos(partidaJogadorNova.getPontosPerdidos());
                                            partidaJogador.setPontosGanhos(partidaJogadorNova.getPontosGanhos());

                                            if (incluir)
                                                dbs.partidaJogadorDAO().insert(partidaJogador);
                                            else
                                                dbs.partidaJogadorDAO().update(partidaJogador);

                                            qtde++;

                                        }

                                    }

                                    Toast.makeText(getBaseContext(), String.valueOf(qtde) + " estatísticas corrigidas.", Toast.LENGTH_LONG).show();

                                }
                            }

                            ).show();

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });

        MenuItem mLimparBase = menu.findItem(R.id.action_limpar);
        mLimparBase.setOnMenuItemClickListener(item -> {

            try {

                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setMessage(getString(R.string.action_apagar_confirmar))
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("OK", (dialogInterface, i) -> {

                            dbs.partidaJogadaDAO().deleteAll();
                            dbs.partidaDAO().deleteAll();
                            dbs.partidaJogadorDAO().deleteAll();
                            dbs.jogadorDAO().deleteAll();

                        }).show();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return false;
        });

        MenuItem mConfiguracoes = menu.findItem(R.id.action_configuracoes);
        mConfiguracoes.setOnMenuItemClickListener(item -> {


            try {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return false;
        });


        MenuItem mSobre = menu.findItem(R.id.action_sobre);
        mSobre.setOnMenuItemClickListener(item -> {


//            try {
//
////                getSupportFragmentManager().beginTransaction()
////                        .add(R.id.nav_host_fragment_content_main, SobreFragment.class, bundle)
////                        .commit();
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//            }

            return false;
        });


        return true;
    }

    private void CarregarIncial() {


        if (dbs.timeDAO().CountAll() == 0) {
            Time time = new Time();
            time.setNome("os Patos");
            dbs.timeDAO().insert(time);

            time = new Time();
            time.setNome("os Marrecos");
            dbs.timeDAO().insert(time);
        }

        if (dbs.jogadorDAO().CountAll() == 0)
            cargaInicialJogadores();


    }

    private List<Jogador> cargaInicialJogadores() {


        Jogador jogador = new Jogador();
        jogador.setOrdem(1);
        jogador.setTimeID(1);
        jogador.setNome("Pato");
        jogador.setAtivo(true);
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(2);
        jogador.setTimeID(2);
        jogador.setNome("Marreco");
        jogador.setAtivo(true);
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(3);
        jogador.setTimeID(1);
        jogador.setNome("Patinho");
        jogador.setAtivo(true);
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(4);
        jogador.setTimeID(2);
        jogador.setNome("Marrequinho");
        jogador.setAtivo(true);
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(5);
        jogador.setTimeID(1);
        jogador.setNome("Patão");
        jogador.setAtivo(true);
        dbs.jogadorDAO().insert(jogador);

        jogador = new Jogador();
        jogador.setOrdem(6);
        jogador.setTimeID(2);
        jogador.setNome("Marrecão");
        jogador.setAtivo(true);
        dbs.jogadorDAO().insert(jogador);

        return dbs.jogadorDAO().getAll();

    }

    private void carregarPartidaJogador(long partidaID) {

        List<Jogador> jogadorList = dbs.jogadorDAO().getAll();
        if (jogadorList == null || (long) jogadorList.size() == 0)
            jogadorList = cargaInicialJogadores();


        for (Jogador jogador : jogadorList) {

            PartidaJogador partidaJogador = new PartidaJogador();
            //partidaJogador.setTimeJogadorID(jogador.getTimeID());
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

}