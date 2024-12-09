package br.com.aj.truco.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaPesquisa;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.generic.GenericAdapter;
import br.com.aj.truco.generic.GenericViewHolder;
import br.com.aj.truco.generic.RecyclerViewListenerHack;

public class PartidasAdapter extends GenericAdapter<PartidaPesquisa, PartidasAdapter.ViewHolder> {

    AppRoomDatabase dbs;
    Context mcontext;

//    public PartidasAdapter(Context context, List<Partida> objects) {
//        super(context, objects);
//        mcontext = context;
//        dbs = AppRoomDatabase.getDatabase(context);
//    }

    public PartidasAdapter(Context context, List<PartidaPesquisa> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
        mcontext = context;
        dbs = AppRoomDatabase.getDatabase(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_partida, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PartidaPesquisa partida = mList.get(position);

        holder.viewPartida.setText(partida.getTitulo());
        holder.viewNomeTime1.setText(partida.getNomeTime1());
        holder.viewNomeTime2.setText(partida.getNomeTime2());
        holder.viewVitoriaTime1.setText(String.valueOf(partida.getVitoriaTime1()));
        holder.viewVitoriaTime2.setText(String.valueOf(partida.getVitoriaTime2()));


        if (partida.getPartidaID() > 0) {

            holder.rvJogadoresTime1.setVisibility(View.VISIBLE);
            holder.rvJogadoresTime2.setVisibility(View.VISIBLE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.rvJogadoresTime1.setLayoutManager(linearLayoutManager);
            holder.rvJogadoresTime1.setHasFixedSize(true);
            List<Jogador> jogadorList = dbs.jogadorDAO().getJogadoresByPartidaTime(partida.getPartidaID(), 1);
            JogadoresTimesAdapter jtaTime1 = new JogadoresTimesAdapter(mcontext, jogadorList, null, null);
            holder.rvJogadoresTime1.setAdapter(jtaTime1);


            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mcontext);
            linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
            holder.rvJogadoresTime2.setLayoutManager(linearLayoutManager2);
            holder.rvJogadoresTime2.setHasFixedSize(true);
            List<Jogador> jogadorList2 = dbs.jogadorDAO().getJogadoresByPartidaTime(partida.getPartidaID(), 2);
            JogadoresTimesAdapter jtaTime2 = new JogadoresTimesAdapter(mcontext, jogadorList2, null, null);
            holder.rvJogadoresTime2.setAdapter(jtaTime2);

        }
        else {
            holder.rvJogadoresTime1.setVisibility(View.GONE);
            holder.rvJogadoresTime2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView viewPartida, viewNomeTime1, viewNomeTime2, viewVitoriaTime1, viewVitoriaTime2;

        private RecyclerView rvJogadoresTime1, rvJogadoresTime2;

        public ViewHolder(View itemView) {
            super(itemView);

            viewPartida = itemView.findViewById(R.id.item_partida_data);
            viewNomeTime1 = itemView.findViewById(R.id.item_partida_time1);
            viewVitoriaTime1 = itemView.findViewById(R.id.item_partida_time1_vitorias);
            viewNomeTime2 = itemView.findViewById(R.id.item_partida_time2);
            viewVitoriaTime2 = itemView.findViewById(R.id.item_partida_time2_vitorias);
            rvJogadoresTime1 = itemView.findViewById(R.id.recycleview_jogador_time1);
            rvJogadoresTime2 = itemView.findViewById(R.id.recycleview_jogador_time2);

        }

        @Override
        public void onClick(View view) {
            PartidasAdapter.this.onClick(view, getAdapterPosition());
        }


        @Override
        public boolean onLongClick(View view) {
            return PartidasAdapter.this.onLongClick(view, getAdapterPosition());
        }

    }
}
