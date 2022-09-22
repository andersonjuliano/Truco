package br.com.aj.truco.generic;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class GenericViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    public GenericViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}

