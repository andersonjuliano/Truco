package br.com.aj.truco.classe;

public class PartidaSelecao {
    public long PartidaID;
    public String Nome;
    public boolean Selecionada;

    public PartidaSelecao(long partidaID, String nome, boolean selecionada) {
        PartidaID = partidaID;
        Nome = nome;
        Selecionada = selecionada;
    }
}
