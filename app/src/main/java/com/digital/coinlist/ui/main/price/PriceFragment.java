package com.digital.coinlist.ui.main.price;

import android.view.LayoutInflater;
import android.view.View;
import androidx.navigation.Navigation;
import com.digital.coinlist.R;
import com.digital.coinlist.databinding.FragmentPriceBinding;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.ui.base.BaseFragment;
import com.digital.coinlist.ui.main.CoinViewModel;
import com.digital.coinlist.ui.main.list.CurrencyType;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PriceFragment extends BaseFragment<FragmentPriceBinding, CoinViewModel> {

    @Override
    protected FragmentPriceBinding getBinding(LayoutInflater inflater) {
        return FragmentPriceBinding.inflate(inflater);
    }

    @Override
    protected Class<CoinViewModel> getViewModelClass() {
        return CoinViewModel.class;
    }

    @Override
    protected boolean createSharedViewModel() {
        return true;
    }

    @Override
    protected int navGraphIdForViewModel() {
        return R.id.coin_nav_graph;
    }

    @Override
    protected void setupView(View view) {
        binding.cvCrypto.setOnClickListener(view1 -> {
            Navigation.findNavController(view1)
                .navigate(PriceFragmentDirections.gotoCoinListFragment(
                    CurrencyType.CRYPTO_CURRENCY));
        });
        binding.cvCurrency.setOnClickListener(view1 -> {
            Navigation.findNavController(view1)
                .navigate(PriceFragmentDirections.gotoCoinListFragment(
                    CurrencyType.VS_CURRENCY));
        });
        binding.swipeLayout.setOnRefreshListener(() -> viewModel.refreshPrice());

        if (getActivity() != null) {
            getActivity().setTitle(R.string.compare);
        }
    }

    @Override
    protected void subscribeToViewModel(CoinViewModel viewModel) {
        viewModel.getPriceComparisonState().observe(this, priceItemUiState -> {
            switch (priceItemUiState.getStatus()) {
                case CREATED:
                case COMPLETE:
                    // to nothing
                    break;
                case SUCCESS:
                    hideProgress();
                    if (priceItemUiState.getData() != null) {
                        handlePriceItem(priceItemUiState.getData());
                    }
                    break;
                case ERROR:
                    handleError();
                    break;
                case LOADING:
                    showProgress();
                    break;
            }
        });

        viewModel.getCombinedLiveData().observe(this,
            pair -> {
                if (pair.first != null) {
                    binding.tvCrypto.setText(pair.first.getName());
                }
                if (pair.second != null) {
                    binding.tvCurrency.setText(pair.second.displayName());
                }
                viewModel.comparePrice(true, true);
            });
    }

    private void handlePriceItem(PriceItem item) {
        binding.tvPriceVal.setText(item.getPrice());
        binding.tvMarketCapVal.setText(item.getMarketCap());
        binding.tvVolumeVal.setText(item.get_24hVol());
        binding.tvChangeVal.setText(item.get_24hChange());
        binding.tvUpdateTimeVal.setText(item.getLastUpdated());
    }

    @Override
    protected void hideProgress() {
        super.hideProgress();
        binding.swipeLayout.setRefreshing(false);
    }

    private void handleError() {
        hideProgress();
        showError(getString(R.string.error_compare));
    }
}
