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

public class TimesAdapter extends GenericAdapter<Time, TimesAdapter.ViewHolder> {


    AppRoomDatabase dbs;

    public TimesAdapter(Context context, List<Time> objects) {
        super(context, objects);
        dbs = AppRoomDatabase.getDatabase(context);
    }

    public TimesAdapter(Context context, List<Time> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
        dbs = AppRoomDatabase.getDatabase(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_times, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Time Time = mList.get(position);
        Time time = dbs.timeDAO().getTime( Time.getTimeID());

        holder.viewNomeTime.setText(Time.getNome());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView  viewNomeTime;


        public ViewHolder(View itemView) {
            super(itemView);

            viewNomeTime = itemView.findViewById(R.id.item_time_nome);
        }

        @Override
        public void onClick(View view) {
            TimesAdapter.this.onClick(view, getAdapterPosition());
        }
    }
}
