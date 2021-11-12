package com.digital.coinlist.ui.main.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.digital.coinlist.ui.main.adapter.CoinListAdapter.CoinItemHolder;
import java.util.List;

public class CoinListAdapter extends RecyclerView.Adapter<CoinItemHolder> {

    private List<Selectable> itemList;


    @NonNull
    @Override
    public CoinItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CoinItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CoinItemHolder extends RecyclerView.ViewHolder{

        public CoinItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
