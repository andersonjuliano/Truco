package br.com.aj.truco.generic;


import android.content.Context;
import android.graphics.drawable.InsetDrawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;



public class RecyclerViewListenerHack {

    public interface OnClickListener<T> {
        void onClickListener(View view, int position, T object);
    }

    public interface OnLongClickListener<T> {
        void onLongPressClickListener(View view, int position, T object);
    }

    public static DividerItemDecoration createDividerFromDrawable(Context context, int orientation, int resId) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, orientation);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, resId));
        return dividerItemDecoration;
    }

    public static DividerItemDecoration createDivider(Context context, int orientation, int insetLeft, int insetRight) {
        return createDivider(context, orientation, insetLeft, 0, insetRight, 0);
    }

    public static DividerItemDecoration createDivider(Context context, int orientation, int insetLeft, int insetTop, int insetRight, int insetBottom) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, orientation);
        InsetDrawable insetDrawable = new InsetDrawable(dividerItemDecoration.getDrawable(),
                insetLeft == 0 ? 0 : convertDpToPx(context, insetLeft),
                insetTop == 0 ? 0 : convertDpToPx(context, insetTop),
                insetRight == 0 ? 0 : convertDpToPx(context, insetRight),
                insetBottom == 0 ? 0 : convertDpToPx(context, insetBottom));

        dividerItemDecoration.setDrawable(insetDrawable);
        return dividerItemDecoration;
    }

        public static int convertDpToPx(Context context, float dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

}
