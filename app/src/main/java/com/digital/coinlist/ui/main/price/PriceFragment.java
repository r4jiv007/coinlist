package com.digital.coinlist.ui.main.price;

import android.view.LayoutInflater;
import android.view.View;
import com.digital.coinlist.databinding.FragmentPriceBinding;
import com.digital.coinlist.ui.base.BaseFragment;

public class PriceFragment extends BaseFragment<FragmentPriceBinding,PriceViewModel> {

    @Override
    protected FragmentPriceBinding getBinding(LayoutInflater inflater) {
        return FragmentPriceBinding.inflate(inflater);
    }

    @Override
    protected Class<PriceViewModel> getViewModelClass() {
        return PriceViewModel.class;
    }

    @Override
    protected void setupView(View view) {

    }

    @Override
    protected void subscribeToViewModel() {

    }
}
