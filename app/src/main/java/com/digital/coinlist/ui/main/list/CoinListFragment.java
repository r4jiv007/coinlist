package com.digital.coinlist.ui.main.list;

import android.view.LayoutInflater;
import android.view.View;
import com.digital.coinlist.databinding.FragmentCoinListBinding;
import com.digital.coinlist.ui.base.BaseFragment;

public class CoinListFragment extends BaseFragment<FragmentCoinListBinding,CoinListViewModel> {

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

    @Override
    protected void subscribeToViewModel() {

    }
}
