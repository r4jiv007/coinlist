package com.digital.coinlist.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.digital.coinlist.databinding.CoinListItemLayoutBinding;
import java.util.List;

public class CoinListAdapter extends RecyclerView.Adapter<CoinItemHolder> implements
    OnClickListener {

    private List<? extends Selectable> itemList;
    private OnItemSelectionListener onItemSelectionListener;

    public CoinListAdapter(List<? extends Selectable> itemList,
        OnItemSelectionListener onItemSelectionListener) {
        this.itemList = itemList;
        this.onItemSelectionListener = onItemSelectionListener;
    }

    public void swapData(List<? extends Selectable> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoinItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CoinItemHolder holder = new CoinItemHolder(CoinListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        ));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoinItemHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        if (pos < 0 && pos >= itemList.size()) {
            return;
        }
        onItemSelectionListener.onItemSelected(itemList.get(pos));
    }
}
