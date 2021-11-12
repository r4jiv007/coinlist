package com.digital.coinlist.ui.main.list;

import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.digital.coinlist.databinding.FragmentCoinListBinding;
import com.digital.coinlist.ui.base.BaseFragment;
import com.digital.coinlist.ui.main.adapter.CoinListAdapter;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.ArrayList;

@AndroidEntryPoint
public class CoinListFragment extends BaseFragment<FragmentCoinListBinding,CoinListViewModel> {

    private CoinListAdapter listAdapter= new CoinListAdapter(new ArrayList<>());

    @Override
    protected FragmentCoinListBinding getBinding(LayoutInflater inflater) {
        return FragmentCoinListBinding.inflate(inflater);
    }

    @Override
    protected Class<CoinListViewModel> getViewModelClass() {
        return CoinListViewModel.class;
    }

    @Override
    protected void setupView(View view) {

    }

    private void initRecyclerView(){
        binding.rcvCoinList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvCoinList.setAdapter(listAdapter);
    }

    @Override
    protected void subscribeToViewModel() {

    }
}
