package br.com.aj.truco.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.PartidaPontos;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.generic.GenericAdapter;
import br.com.aj.truco.generic.GenericViewHolder;
import br.com.aj.truco.generic.RecyclerViewListenerHack;

public class PartidaPontosAdapter extends GenericAdapter<PartidaPontos, PartidaPontosAdapter.ViewHolder> {

    AppRoomDatabase dbs;
    Context mcontext;

    public PartidaPontosAdapter(Context context, List<PartidaPontos> objects) {
        super(context, objects);
        mcontext = context;
        dbs = AppRoomDatabase.getDatabase(context);
    }

    public PartidaPontosAdapter(Context context, List<PartidaPontos> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
        mcontext = context;
        dbs = AppRoomDatabase.getDatabase(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_partida_pontos_jogados, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PartidaPontos partidaPontos = mList.get(position);

        holder.viewID.setText(String.valueOf(partidaPontos.PartidaJogadaID));

        holder.viewTime1Nome.setText(partidaPontos.Time1Nome + ": ");
        holder.viewTime1Pontos.setText(String.valueOf(partidaPontos.PontosTime1));
        holder.viewTime1Partidas.setText(String.valueOf(partidaPontos.VitoriasTime1));

        if (partidaPontos.PontosTime1 >= 12)
            holder.viewTime1Partidas.setText(String.valueOf(partidaPontos.VitoriasTime1 + 1));


        holder.viewTime2Nome.setText(partidaPontos.Time2Nome + ": ");
        holder.viewTime2Pontos.setText(String.valueOf(partidaPontos.PontosTime2));
        holder.viewTime2Partidas.setText(String.valueOf(partidaPontos.VitoriasTime2));
        if (partidaPontos.PontosTime2 >= 12)
            holder.viewTime2Partidas.setText(String.valueOf(partidaPontos.VitoriasTime2 + 1));


        holder.viewJogadorNome.setText(String.valueOf(partidaPontos.JogadorNome));
        holder.viewJogadorPontos.setText(String.valueOf(partidaPontos.JogadorPontos));
        holder.viewJogadorPartidas.setText(String.valueOf(partidaPontos.JogadorPartidas));

        if (partidaPontos.Vitoria) {
            holder.viewPonto.setText("+" + String.valueOf(partidaPontos.Pontos));
            holder.viewPonto.setTextColor(Color.BLUE);
        } else {
            holder.viewPonto.setText("-" + String.valueOf(partidaPontos.Pontos));
            holder.viewPonto.setTextColor(Color.RED);
        }
        if (partidaPontos.JogadorPontos < 0)
            holder.viewJogadorPontos.setTextColor(Color.RED);
        else
            holder.viewJogadorPontos.setTextColor(Color.BLACK);

        if (partidaPontos.JogadorPartidas < 0)
            holder.viewJogadorPartidas.setTextColor(Color.RED);
        else
            holder.viewJogadorPartidas.setTextColor(Color.BLACK);

//        if (partidaPontos.PontosTime1 >= 12 || partidaPontos.PontosTime2 >= 12) {
////            holder.viewLinha.setVisibility(View.VISIBLE);
//            if (partidaPontos.PontosTime1 >= 12) {
//                holder.viewTime1Partidas.setText(String.valueOf(partidaPontos.VitoriasTime1 + 1));
//                holder.viewTime1Partidas.setTextColor(Color.BLUE);
//                holder.viewTime1Nome.setTextColor(Color.BLUE);
//                holder.viewTime1Pontos.setTextColor(Color.BLUE);
//            } else {
//                holder.viewTime2Partidas.setText(String.valueOf(partidaPontos.VitoriasTime2 + 1));
//                holder.viewTime2Partidas.setTextColor(Color.BLUE);
//                holder.viewTime2Nome.setTextColor(Color.BLUE);
//                holder.viewTime2Partidas.setTextColor(Color.BLUE);
//            }
//
//            holder.viewID.setTypeface(null, Typeface.BOLD);
//            holder.viewID.setPadding(0, 0, 8, 16);
//            holder.viewTime1Nome.setTypeface(null, Typeface.BOLD);
//            holder.viewTime1Pontos.setTypeface(null, Typeface.BOLD);
//            holder.viewTime1Partidas.setTypeface(null, Typeface.BOLD);
//
//            holder.viewTime2Nome.setTypeface(null, Typeface.BOLD);
//            holder.viewTime2Pontos.setTypeface(null, Typeface.BOLD);
//            holder.viewTime2Partidas.setTypeface(null, Typeface.BOLD);
//
//            holder.viewJogadorNome.setTypeface(null, Typeface.BOLD);
//            holder.viewJogadorPontos.setTypeface(null, Typeface.BOLD);
//            holder.viewJogadorPartidas.setTypeface(null, Typeface.BOLD);
//            holder.viewPonto.setTypeface(null, Typeface.BOLD);
//        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView viewTime1Nome, viewTime1Pontos, viewTime1Partidas,
                viewTime2Nome, viewTime2Pontos, viewTime2Partidas,
                viewJogadorNome, viewJogadorPontos, viewJogadorPartidas,
                viewID, viewPonto;
        private View viewLinha;


        public ViewHolder(View itemView) {
            super(itemView);


            viewID = itemView.findViewById(R.id.item_partida_pontos_id);

            viewTime1Nome = itemView.findViewById(R.id.item_partida_pontos_time1_nome);
            viewTime1Pontos = itemView.findViewById(R.id.item_partida_pontos_time1_pontos);
            viewTime1Partidas = itemView.findViewById(R.id.item_partida_pontos_time1_partidas);

            viewTime2Nome = itemView.findViewById(R.id.item_partida_pontos_time2_nome);
            viewTime2Pontos = itemView.findViewById(R.id.item_partida_pontos_time2_pontos);
            viewTime2Partidas = itemView.findViewById(R.id.item_partida_pontos_time2_partidas);

            viewJogadorNome = itemView.findViewById(R.id.item_partida_pontos_jogador_nome);
            viewJogadorPontos = itemView.findViewById(R.id.item_partida_pontos_jogador_pontos);
            viewJogadorPartidas = itemView.findViewById(R.id.item_partida_pontos_jogador_partidas);

            viewLinha = itemView.findViewById(R.id.item_partida_pontos_linha);
            viewPonto = itemView.findViewById(R.id.item_partida_pontos_partida_ponto);


        }

        @Override
        public void onClick(View view) {
            PartidaPontosAdapter.this.onClick(view, getAdapterPosition());
        }

    }
}
