package br.com.aj.truco.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.generic.GenericAdapter;
import br.com.aj.truco.generic.GenericViewHolder;
import br.com.aj.truco.generic.RecyclerViewListenerHack;

public class ResultadoPartidasAdapter extends GenericAdapter<PartidaJogada, ResultadoPartidasAdapter.ViewHolder> {


    AppRoomDatabase dbs;
    Partida partida;

    public ResultadoPartidasAdapter(Context context, List<PartidaJogada> objects) {
        super(context, objects);
        dbs = AppRoomDatabase.getDatabase(context);
    }

    public ResultadoPartidasAdapter(Context context, List<PartidaJogada> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener, Partida _partida) {
        super(context, objects, clickListener, longClickListener);
        dbs = AppRoomDatabase.getDatabase(context);
        partida = _partida;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_resultado_partidas, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PartidaJogada partidaJogada = mList.get(position);

        long pontos1 = partidaJogada.getPontosTime1();
        long pontos2 = partidaJogada.getPontosTime2();

        if ((partidaJogada.getTimeID() == partida.getTime1ID() && partidaJogada.isVitoria())
                || (partidaJogada.getTimeID() == partida.getTime2ID() && !partidaJogada.isVitoria())) {
            pontos1 += partidaJogada.getPontos();
        } else {
            pontos2 += partidaJogada.getPontos();
        }

        holder.textPontosTime1.setText(String.valueOf(pontos1));
        holder.textPontosTime2.setText(String.valueOf(pontos2));

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.textColorPositivo, typedValue, true);
        int textColorPositivo = ContextCompat.getColor(mContext, typedValue.resourceId);

        mContext.getTheme().resolveAttribute(R.attr.textColorNegativo, typedValue, true);
        int textColorNegativo = ContextCompat.getColor(mContext, typedValue.resourceId);

        if (pontos2 >= 12) {
            holder.textPontosTime1.setTextColor(textColorNegativo);
            holder.textPontosTime2.setTextColor(textColorPositivo);
        } else {
            holder.textPontosTime1.setTextColor(textColorPositivo);
            holder.textPontosTime2.setTextColor(textColorNegativo);
        }


        if (pontos1 == 0 || pontos2 == 0) {
            holder.textPontosTime1.setTypeface(null, Typeface.BOLD);
            holder.textPontosTime2.setTypeface(null, Typeface.BOLD);
        } else {
            holder.textPontosTime1.setTypeface(null, Typeface.NORMAL);
            holder.textPontosTime2.setTypeface(null, Typeface.NORMAL);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView textPontosTime1, textPontosTime2;


        public ViewHolder(View itemView) {
            super(itemView);

            textPontosTime1 = itemView.findViewById(R.id.item_resultado_partidas_pontos_time_1);
            textPontosTime2 = itemView.findViewById(R.id.item_resultado_partidas_pontos_time_2);

        }
    }
}
