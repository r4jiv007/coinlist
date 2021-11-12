package com.digital.coinlist.ui.main.adapter;

import androidx.recyclerview.widget.RecyclerView;
import com.digital.coinlist.databinding.CoinListItemLayoutBinding;

public class CoinItemHolder extends RecyclerView.ViewHolder {

    private final CoinListItemLayoutBinding binding;

    public CoinItemHolder(CoinListItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Selectable item) {
        binding.tvItemName.setText(item.displayName());
    }
}
