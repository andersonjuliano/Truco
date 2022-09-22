package br.com.aj.truco.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.MySingletonClass;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.generic.GenericAdapter;
import br.com.aj.truco.generic.GenericViewHolder;
import br.com.aj.truco.generic.RecyclerViewListenerHack;

public class PartidaJogadoresAdapter extends GenericAdapter<PartidaJogador, PartidaJogadoresAdapter.ViewHolder> {

    private List<Jogador> jogadores;

    public PartidaJogadoresAdapter(Context context, List<PartidaJogador> objects) {
        super(context, objects);
    }

    public PartidaJogadoresAdapter(Context context, List<PartidaJogador> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_partida_jogador, parent, false);
        jogadores = MySingletonClass.getInstance().getJogadores();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PartidaJogador partidaJogador = mList.get(position);
        Jogador jogador = jogadores.stream().filter(x -> x.getJogadorID() == partidaJogador.getJogadorID()).findFirst().orElse(null);

        holder.viewNomeJogador.setText(jogador.getNome());
        holder.viewPontosVitoria.setText(String.valueOf(partidaJogador.getVitoria()));
        holder.viewPontosDerrota.setText(String.valueOf(partidaJogador.getDerrota()));
        holder.viewPartidasVitoria.setText(String.valueOf(partidaJogador.getPontosGanhos()));
        holder.viewPartidasDerrota.setText(String.valueOf(partidaJogador.getPontosPerdidos()));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView viewNomeJogador, viewPontosVitoria, viewPontosDerrota, viewPartidasVitoria, viewPartidasDerrota;


        public ViewHolder(View itemView) {
            super(itemView);

            viewNomeJogador = itemView.findViewById(R.id.item_partida_nome_jogador);
            viewPontosVitoria = itemView.findViewById(R.id.item_partida_pontos_vitoria);
            viewPontosDerrota = itemView.findViewById(R.id.item_partida_partidas_derrota);
            viewPartidasVitoria = itemView.findViewById(R.id.item_partida_partidas_vitoria);
            viewPartidasDerrota = itemView.findViewById(R.id.item_partida_pontos_derrota);

        }

        @Override
        public void onClick(View view) {
            PartidaJogadoresAdapter.this.onClick(view, getAdapterPosition());
        }
    }
}
