package br.com.aj.truco.adapter;

import android.content.Context;
import android.graphics.Color;
import android.provider.Telephony;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.generic.GenericAdapter;
import br.com.aj.truco.generic.GenericViewHolder;
import br.com.aj.truco.generic.RecyclerViewListenerHack;

public class PartidasDoJogadorAdapter extends GenericAdapter<PartidaJogador, PartidasDoJogadorAdapter.ViewHolder> {

    AppRoomDatabase dbs;
    Context mcontext;
    public PartidasDoJogadorAdapter(Context context, List<PartidaJogador> objects) {
        super(context, objects);
        mcontext= context;
        dbs = AppRoomDatabase.getDatabase(context);
    }

    public PartidasDoJogadorAdapter(Context context, List<PartidaJogador> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
        mcontext= context;
        dbs = AppRoomDatabase.getDatabase(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_partida_jogador, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PartidaJogador partidaJogador = mList.get(position);
        Partida partida = dbs.partidaDAO().getPartida(partidaJogador.getPartidaID());

        holder.viewNomeJogador.setText(partida.getTitulo());

        holder.viewPontosVitoria.setText(String.valueOf(partidaJogador.getPontosGanhos()));
        holder.viewPontosDerrota.setText(String.valueOf(partidaJogador.getPontosPerdidos()));
        holder.viewPontosSaldo.setText(String.valueOf(partidaJogador.SaldoPontos()));

        TypedValue typedValue = new TypedValue();
        mcontext.getTheme().resolveAttribute(R.attr.textColorPositivo, typedValue, true);
        int textColorPositivo = ContextCompat.getColor(mcontext, typedValue.resourceId);

        mcontext.getTheme().resolveAttribute(R.attr.textColorNegativo, typedValue, true);
        int textColorNegativo = ContextCompat.getColor(mcontext, typedValue.resourceId);


        if ((partidaJogador.SaldoPontos() ) < 0)
            holder.viewPontosSaldo.setTextColor(textColorNegativo);
        else if ((partidaJogador.SaldoPontos()) > 0)
            holder.viewPontosSaldo.setTextColor(textColorPositivo);

        holder.viewPartidasVitoria.setText(String.valueOf(partidaJogador.getVitoria()));
        holder.viewPartidasDerrota.setText(String.valueOf(partidaJogador.getDerrota()));
        holder.viewPartidasSaldo.setText(String.valueOf(partidaJogador.SaldoVitorias()));
        if ((partidaJogador.SaldoVitorias()) < 0)
            holder.viewPartidasSaldo.setTextColor(textColorNegativo);
        else if ((partidaJogador.SaldoVitorias()) > 0)
            holder.viewPartidasSaldo.setTextColor(textColorPositivo);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView viewNomeJogador, viewPontosVitoria, viewPontosDerrota, viewPontosSaldo, viewPartidasVitoria, viewPartidasDerrota, viewPartidasSaldo;


        public ViewHolder(View itemView) {
            super(itemView);

            viewNomeJogador = itemView.findViewById(R.id.item_partida_nome_jogador);

            viewPontosVitoria = itemView.findViewById(R.id.item_partida_pontos_vitoria);
            viewPontosDerrota = itemView.findViewById(R.id.item_partida_pontos_derrota);
            viewPontosSaldo = itemView.findViewById(R.id.item_partida_pontos_saldo);

            viewPartidasVitoria = itemView.findViewById(R.id.item_partida_partidas_vitoria);
            viewPartidasDerrota = itemView.findViewById(R.id.item_partida_partidas_derrota);
            viewPartidasSaldo = itemView.findViewById(R.id.item_partida_partidas_saldo);

        }

        @Override
        public void onClick(View view) {
            PartidasDoJogadorAdapter.this.onClick(view, getAdapterPosition());
        }
    }
}
