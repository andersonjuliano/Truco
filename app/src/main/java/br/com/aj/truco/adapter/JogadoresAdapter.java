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


public class JogadoresAdapter extends GenericAdapter<Jogador, JogadoresAdapter.ViewHolder> {


    AppRoomDatabase dbs;

    public JogadoresAdapter(Context context, List<Jogador> objects) {
        super(context, objects);
        dbs = AppRoomDatabase.getDatabase(context);
    }

    public JogadoresAdapter(Context context, List<Jogador> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
        dbs = AppRoomDatabase.getDatabase(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_jogador, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Jogador jogador = mList.get(position);
        Time time = dbs.timeDAO().getTime( jogador.getTimeID());

        holder.viewNome.setText(jogador.getNome());
        holder.viewNomeTime.setText(" - ");

        if (time != null)
            holder.viewNomeTime.setText(time.getNome());


        holder.viewSequencia.setText(String.valueOf(jogador.getOrdem()));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView viewNome, viewSequencia, viewNomeTime;


        public ViewHolder(View itemView) {
            super(itemView);

            viewNome = itemView.findViewById(R.id.item_nome_jogador);
            viewSequencia = itemView.findViewById(R.id.item_sequencia_jogador);
            viewNomeTime = itemView.findViewById(R.id.item_time_jogador);
        }

        @Override
        public void onClick(View view) {
            JogadoresAdapter.this.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return JogadoresAdapter.this.onLongClick(view, getAdapterPosition());
        }
    }
}
