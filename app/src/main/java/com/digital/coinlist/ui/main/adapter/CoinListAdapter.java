package com.digital.coinlist.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.digital.coinlist.databinding.CoinListItemLayoutBinding;
import com.digital.coinlist.ui.main.adapter.CoinListAdapter.CoinItemHolder;
import java.util.List;

public class CoinListAdapter extends RecyclerView.Adapter<CoinItemHolder> {

    private List<? extends Selectable> itemList;

    public CoinListAdapter(List<? extends Selectable> itemList) {
        this.itemList = itemList;
    }

    public void swapData(List<? extends Selectable> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoinItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CoinItemHolder(CoinListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        ));
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

    protected static class CoinItemHolder extends RecyclerView.ViewHolder {

        private final CoinListItemLayoutBinding binding;

        public CoinItemHolder(CoinListItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Selectable item) {
            binding.tvItemName.setText(item.displayName());
        }
    }
}
