package br.com.aj.truco.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.Time;
import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.generic.GenericAdapter;
import br.com.aj.truco.generic.GenericViewHolder;
import br.com.aj.truco.generic.RecyclerViewListenerHack;
import br.com.aj.truco.ui.PartidaPontosActivity;
import br.com.aj.truco.util.SharedPreferencesUtil;

public class EstatisticasPorJogadorAdapter extends GenericAdapter<Jogador, EstatisticasPorJogadorAdapter.ViewHolder> {


    AppRoomDatabase dbs;
    private Context ctx;

    public EstatisticasPorJogadorAdapter(Context context, List<Jogador> objects) {
        super(context, objects);
        dbs = AppRoomDatabase.getDatabase(context);
        ctx = context;
    }

    public EstatisticasPorJogadorAdapter(Context context, List<Jogador> objects, RecyclerViewListenerHack.OnClickListener clickListener, RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        super(context, objects, clickListener, longClickListener);
        dbs = AppRoomDatabase.getDatabase(context);
        ctx = context;
    }

    @Override
    public EstatisticasPorJogadorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.estatistica_por_jogador, parent, false);
        return new EstatisticasPorJogadorAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EstatisticasPorJogadorAdapter.ViewHolder holder, int position) {
        Jogador jogador = mList.get(position);

        holder.viewNome.setText(jogador.getNome());


        List<PartidaJogador> partidaJogadores = dbs.partidaJogadorDAO().getLastByJogador(jogador.getJogadorID(), 20);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setHasFixedSize(true);

        PartidasDoJogadorAdapter   adapter = new PartidasDoJogadorAdapter(ctx, partidaJogadores, listClickListener, null);
        holder.recyclerView.setAdapter(adapter);

    }

    private RecyclerViewListenerHack.OnClickListener listClickListener = new RecyclerViewListenerHack.OnClickListener<Object>() {
        @Override
        public void onClickListener(View view, int position, Object object) {

            if (object instanceof PartidaJogador) {

                Intent intent = new Intent(ctx, PartidaPontosActivity.class);
                intent.putExtra(Partida.EXTRA_KEY,  ((PartidaJogador) object).getPartidaID());
                intent.putExtra(Jogador.EXTRA_KEY,  ((PartidaJogador) object).getJogadorID());
                ctx.startActivity(intent);

            }
        }
    };

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends GenericViewHolder {

        private TextView viewNome;
        private RecyclerView recyclerView;


        public ViewHolder(View itemView) {
            super(itemView);

            viewNome = itemView.findViewById(R.id.estatistica_por_jogador_nome);
            recyclerView =itemView.findViewById(R.id.estatistica_por_jogador_recycleview);

        }

        @Override
        public void onClick(View view) {
            EstatisticasPorJogadorAdapter.this.onClick(view, getAdapterPosition());
        }
    }
}
