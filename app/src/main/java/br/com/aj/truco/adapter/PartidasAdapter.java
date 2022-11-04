package br.com.aj.truco.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.generic.GenericAdapter;
import br.com.aj.truco.generic.GenericViewHolder;
import br.com.aj.truco.generic.RecyclerViewListenerHack;

public class PartidasAdapter extends GenericAdapter<Partida, PartidasAdapter.ViewHolder> {


    AppRoomDatabase dbs;

    public PartidasAdapter(Context context, List<Partida> objects) {
        super(context, objects);
        dbs = AppRoomDatabase.getDatabase(context);
    }

    public PartidasAdapter(Context context, List<Partida> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
        dbs = AppRoomDatabase.getDatabase(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_partida, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Partida partida = mList.get(position);

        holder.viewPartida.setText(partida.getTitulo());
        holder.viewNomeTime1.setText(dbs.timeDAO().getTime(1).getNome() + ":");
        holder.viewNomeTime2.setText(dbs.timeDAO().getTime(2).getNome() +":");
        holder.viewVitoriaTime1.setText(String.valueOf(partida.getVitoriaTime1()));
        holder.viewVitoriaTime2.setText(String.valueOf(partida.getVitoriaTime2()));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView viewPartida, viewNomeTime1, viewNomeTime2, viewVitoriaTime1, viewVitoriaTime2;


        public ViewHolder(View itemView) {
            super(itemView);

            viewPartida = itemView.findViewById(R.id.item_partida_data);
            viewNomeTime1 = itemView.findViewById(R.id.item_partida_time1);
            viewVitoriaTime1 = itemView.findViewById(R.id.item_partida_time1_vitorias);
            viewNomeTime2 = itemView.findViewById(R.id.item_partida_time2);
            viewVitoriaTime2 = itemView.findViewById(R.id.item_partida_time2_vitorias);

        }

        @Override
        public void onClick(View view) {
            PartidasAdapter.this.onClick(view, getAdapterPosition());
        }
    }
}
