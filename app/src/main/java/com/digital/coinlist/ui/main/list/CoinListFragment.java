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
public class CoinListFragment extends BaseFragment<FragmentCoinListBinding, CoinListViewModel> {

    private final CoinListAdapter listAdapter = new CoinListAdapter(new ArrayList<>());

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
        initRecyclerView();
    }

    private void initRecyclerView() {
        binding.rcvCoinList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvCoinList.setAdapter(listAdapter);
    }

    @Override
    protected void subscribeToViewModel(CoinListViewModel viewModel) {
        viewModel.getCoinListData().observe(this, coinListUiState -> {
            switch (coinListUiState.getStatus()) {
                case CREATED:
                case COMPLETE:
                    break;
                case LOADING:
                    break;
                case SUCCESS:
                    listAdapter.swapData(coinListUiState.getData());
                    break;
                case ERROR:
                    break;
            }
        });
    }
}
