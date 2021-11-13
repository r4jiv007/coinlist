package com.digital.coinlist.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.digital.coinlist.databinding.CoinListItemLayoutBinding;
import java.util.ArrayList;
import java.util.List;

public class CoinListAdapter extends RecyclerView.Adapter<CoinItemHolder> implements
    OnClickListener, Filterable {

    private List<? extends Selectable> itemList;
    private List<? extends Selectable> filteredItemList;
    private OnItemSelectionListener onItemSelectionListener;
    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public CoinListAdapter(List<? extends Selectable> itemList,
        OnItemSelectionListener onItemSelectionListener) {
        this.itemList = itemList;
        this.filteredItemList = itemList;
        this.onItemSelectionListener = onItemSelectionListener;
    }

    public void swapData(List<? extends Selectable> itemList) {
        this.itemList = itemList;
        this.filteredItemList = itemList;
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
        holder.bind(filteredItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredItemList.size();
    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        if (pos < 0 && pos >= filteredItemList.size()) {
            return;
        }
        onItemSelectionListener.onItemSelected(filteredItemList.get(pos));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredItemList = itemList;
                } else {
                    List<Selectable> filteredList = new ArrayList<>();
                    for (Selectable item : itemList) {
                        if (item.displayName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                    filteredItemList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItemList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItemList = (ArrayList<? extends Selectable>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
