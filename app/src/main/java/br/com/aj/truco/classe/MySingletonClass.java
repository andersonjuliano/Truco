package br.com.aj.truco.classe;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class MySingletonClass {

    private static MySingletonClass instance;

    public static MySingletonClass getInstance() {
        if (instance == null)
            instance = new MySingletonClass();
        return instance;
    }

    private MySingletonClass() {
    }

    private List<Time> times;
    private List<Jogador> jogadores;
    private Jogador jogadorPe;
    private List<PartidaJogador> partidaJogadores;
    private List<Partida> partidas;
    private Partida partida = new Partida();
    private UltimaPartida ultimaPartida = new UltimaPartida();



    public static void setInstance(MySingletonClass instance) {
        MySingletonClass.instance = instance;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public Jogador getJogadorPe() {
        return jogadorPe;
    }

    public void setJogadorPe(Jogador jogadorPe) {
        this.jogadorPe = jogadorPe;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public List<PartidaJogador> getTimeJogadores() {
        return partidaJogadores;
    }

    public void setTimeJogadores(List<PartidaJogador> partidaJogadores) {
        this.partidaJogadores = partidaJogadores;
    }

    public List<PartidaJogador> getPartidaJogadores() {
        return partidaJogadores;
    }

    public void setPartidaJogadores(List<PartidaJogador> partidaJogadores) {
        this.partidaJogadores = partidaJogadores;
    }

    public UltimaPartida getUltimaPartida() {
        return ultimaPartida;
    }

    public void setUltimaPartida(UltimaPartida ultimaPartida) {
        this.ultimaPartida = ultimaPartida;
    }

    public void CarregarIncial() {

        if (partidas == null){
            partidas = new ArrayList<>();

            partida.setPartidaID(1);
            partida.setDataPartida(Calendar.getInstance().getTime());

            partidas.add(partida);
        }

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
            partidaJogadores = new ArrayList<>();

            Jogador jogador = new Jogador();
            jogador.setJogadorID(1);
            jogador.setOrdem(1);
            jogador.setTimeID(1);
            jogador.setNome("Juliano");

            jogadores.add(jogador);
            PartidaJogador partidaJogador = new PartidaJogador();
            partidaJogador.setTimeJogadorID(1);
            partidaJogador.setJogadorID(jogador.getJogadorID());
            partidaJogador.setPartidaID(partida.getPartidaID());
            partidaJogadores.add(partidaJogador);


            jogador = new Jogador();
            jogador.setJogadorID(2);
            jogador.setOrdem(2);
            jogador.setTimeID(2);
            jogador.setNome("Nelson");
            jogadores.add(jogador);

            partidaJogador = new PartidaJogador();
            partidaJogador.setTimeJogadorID(2);
            partidaJogador.setJogadorID(jogador.getJogadorID());
            partidaJogador.setPartidaID(partida.getPartidaID());
            partidaJogadores.add(partidaJogador);


            jogador = new Jogador();
            jogador.setJogadorID(3);
            jogador.setOrdem(3);
            jogador.setTimeID(1);
            jogador.setNome("Eder");
            jogadores.add(jogador);

            partidaJogador = new PartidaJogador();
            partidaJogador.setTimeJogadorID(3);
            partidaJogador.setJogadorID(jogador.getJogadorID());
            partidaJogador.setPartidaID(partida.getPartidaID());
            partidaJogadores.add(partidaJogador);


            jogador = new Jogador();
            jogador.setJogadorID(4);
            jogador.setOrdem(4);
            jogador.setTimeID(2);
            jogador.setNome("Genêsio");
            jogadores.add(jogador);

            partidaJogador = new PartidaJogador();
            partidaJogador.setTimeJogadorID(4);
            partidaJogador.setJogadorID(jogador.getJogadorID());
            partidaJogador.setPartidaID(partida.getPartidaID());
            partidaJogadores.add(partidaJogador);


            jogador = new Jogador();
            jogador.setJogadorID(5);
            jogador.setOrdem(5);
            jogador.setTimeID(1);
            jogador.setNome("Gustavo");
            jogadores.add(jogador);

            partidaJogador = new PartidaJogador();
            partidaJogador.setTimeJogadorID(5);
            partidaJogador.setJogadorID(jogador.getJogadorID());
            partidaJogador.setPartidaID(partida.getPartidaID());
            partidaJogadores.add(partidaJogador);


            jogador = new Jogador();
            jogador.setJogadorID(6);
            jogador.setOrdem(6);
            jogador.setTimeID(2);
            jogador.setNome("Zé");
            jogadores.add(jogador);

            partidaJogador = new PartidaJogador();
            partidaJogador.setTimeJogadorID(6);
            partidaJogador.setJogadorID(jogador.getJogadorID());
            partidaJogador.setPartidaID(partida.getPartidaID());
            partidaJogadores.add(partidaJogador);

        }

    }
}
