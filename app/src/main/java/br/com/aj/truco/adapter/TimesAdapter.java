package br.com.aj.truco.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

import br.com.aj.truco.R;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Time time = mList.get(position);
//        Time time = dbs.timeDAO().getTime( Time.getTimeID());

        holder.textNomeTime.setText(time.getNome());
        if (time.isAtivo()) {
            holder.cardTime.setForeground(mContext.getResources().getDrawable(R.drawable.cardview_border_ativo));
        }else{
//            holder.cardTime.setForeground(mContext.getResources().getDrawable(R.drawable.cardview_border_inativo));
            holder.cardTime.setForeground(null);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView textNomeTime;
        private CardView cardTime;

        public ViewHolder(View itemView) {
            super(itemView);

            textNomeTime = itemView.findViewById(R.id.item_time_nome);
            cardTime = itemView.findViewById(R.id.item_card_time);
        }

        @Override
        public void onClick(View view) {
            TimesAdapter.this.onClick(view, getAdapterPosition());
        }
        @Override
        public boolean onLongClick(View view) {
            TimesAdapter.this.onLongClick(view, getAdapterPosition());
            return true;
        }
    }
}
