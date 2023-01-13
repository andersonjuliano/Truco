package br.com.aj.truco.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.generic.GenericAdapter;
import br.com.aj.truco.generic.GenericViewHolder;
import br.com.aj.truco.generic.RecyclerViewListenerHack;

public class JogadoresTimesAdapter extends GenericAdapter<Jogador, JogadoresTimesAdapter.ViewHolder> {


    AppRoomDatabase dbs;

    public JogadoresTimesAdapter(Context context, List<Jogador> objects) {
        super(context, objects);
        dbs = AppRoomDatabase.getDatabase(context);
    }

    public JogadoresTimesAdapter(Context context, List<Jogador> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
        dbs = AppRoomDatabase.getDatabase(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_partida_jogador_time, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Jogador jogador = mList.get(position);
        holder.viewNomeJogador.setText(jogador.getNome());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView  viewNomeJogador;


        public ViewHolder(View itemView) {
            super(itemView);

            viewNomeJogador = itemView.findViewById(R.id.item_nome_jogador_time);
        }

        @Override
        public void onClick(View view) {
            JogadoresTimesAdapter.this.onClick(view, getAdapterPosition());
        }
    }
}
