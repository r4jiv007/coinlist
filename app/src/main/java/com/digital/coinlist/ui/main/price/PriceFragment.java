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
            Navigation.findNavController(view1).navigate(PriceFragmentDirections.gotoCoinListFragment(
                CurrencyType.CRYPTO_CURRENCY));
        });
        binding.cvCurrency.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(PriceFragmentDirections.gotoCoinListFragment(
                CurrencyType.VS_CURRENCY));
        });
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
                    assert priceItemUiState.getData() != null;
                    handlePriceItem(priceItemUiState.getData());
                    break;
                case ERROR:
                    break;
                case LOADING:
                    break;
            }
        });
    }

    private void handlePriceItem(PriceItem item) {
        binding.tvPriceVal.setText(String.valueOf(item.getPrice()));
        binding.tvMarketCapVal.setText(String.valueOf(item.getMarketCap()));
        binding.tvVolumeVal.setText(String.valueOf(item.get_24hVol()));
        binding.tvChangeVal.setText(String.valueOf(item.get_24hChange()));
        binding.tvUpdateTimeVal.setText(String.valueOf(item.getLastUpdated()));
    }
}
