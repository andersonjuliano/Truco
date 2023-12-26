package br.com.aj.truco.generic;


import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class GenericAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final String TAG = GenericAdapter.class.getSimpleName();

    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected List<T> mList;

    protected SparseArray<T> mSelectedItems;

    protected br.com.aj.truco.generic.RecyclerViewListenerHack.OnClickListener mOnClickListener;
    protected br.com.aj.truco.generic.RecyclerViewListenerHack.OnLongClickListener mOnLongClickListener;

    public GenericAdapter(Context context, List<T> objects) {
        this(context, objects, null, null);
    }

    public GenericAdapter(Context context, List<T> objects, br.com.aj.truco.generic.RecyclerViewListenerHack.OnClickListener clickListener, br.com.aj.truco.generic.RecyclerViewListenerHack.OnLongClickListener longClickListener) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.mList = objects;
        this.mSelectedItems = new SparseArray<>();

        this.mOnClickListener = clickListener;
        this.mOnLongClickListener = longClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //region Auxiliares

    public List<T> getList() {
        return mList;
    }

    public T getObject(int position) {
        return mList.get(position);
    }

    public void add(T t) {
        mList.add(t);
        notifyItemInserted(mList.size() - 1);
    }

    public void addItem(int position, T t) {
        mList.add(position, t);
        notifyItemInserted(position);
    }

    public void addAll(List<T> list) {
        int position = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(position, list.size());
    }

    public void addAll(int position, List<T> list) {
        mList.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    public void changeItem(int position, T t) {
        mList.set(position, t);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void setList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    //endregion

    //region "Listeners" Auxiliares

    public void setOnClickListener(br.com.aj.truco.generic.RecyclerViewListenerHack.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void setOnLongClickListener(br.com.aj.truco.generic.RecyclerViewListenerHack.OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    protected void onClick(View v, int position) {
        if (mOnClickListener != null) {
            mOnClickListener.onClickListener(v, position, mList.get(position));
        }
    }

    protected boolean onLongClick(View v, int position) {
        if (mOnLongClickListener != null) {
            mOnLongClickListener.onLongPressClickListener(v, position, mList.get(position));
            return true;
        }
        return false;
    }

    //endregion

    //region CAB (Context Action Bar) - Selections

    //Toggle selection methods
    public void toggleSelection(int position, T value) {
        selectView(position, value, mSelectedItems.get(position) == null);
    }

    //Remove selected selections
    public void removeSelection() {
        mSelectedItems = new SparseArray();
        notifyDataSetChanged();
    }

    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, T value, boolean checked) {
        if (checked)
            mSelectedItems.put(position, value);
        else
            mSelectedItems.remove(position);

        notifyDataSetChanged();
    }

    public void selectAll() {
        for (int i = 0; i < mList.size(); i++) {
            mSelectedItems.put(i, mList.get(i));
        }

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItems.size();
    }

    //Return all selected ids
    public SparseArray<T> getSelectedItems() {
        return mSelectedItems;
    }

    public boolean allSelected() {
        return (mSelectedItems.size() == mList.size());
    }

    //endregion

}

