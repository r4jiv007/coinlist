package com.digital.coinlist.ui.main.price;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.digital.coinlist.R;
import com.digital.coinlist.databinding.FragmentPriceBinding;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.ui.base.BaseFragment;
import com.digital.coinlist.ui.main.list.CurrencyType;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PriceFragment extends BaseFragment<FragmentPriceBinding, PriceViewModel> {


    public static final String COIN = "coin";
    public static final String CURRENCY = "currency";

    @Override
    protected FragmentPriceBinding getBinding(LayoutInflater inflater) {
        return FragmentPriceBinding.inflate(inflater);
    }

    @Override
    protected Class<PriceViewModel> getViewModelClass() {
        return PriceViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = NavHostFragment.findNavController(this);
        // We use a String here, but any type that can be put in a Bundle is supported
        if (navController.getCurrentBackStackEntry() == null) {
            return;
        }
        listenToCoinItemSelection();
        listentoCurrencyItemSelection();

    }

    private void listenToCoinItemSelection() {
        MutableLiveData<String> liveData = findNavController().getCurrentBackStackEntry()
            .getSavedStateHandle()
            .getLiveData(COIN);
        liveData.observe(getViewLifecycleOwner(),
            (Observer<Object>) coinItem -> {
                CoinItem item = (CoinItem) coinItem;
                viewModel.submitCoin(item);
                binding.tvCrypto.setText(item.getName());
            });
    }

    private void listentoCurrencyItemSelection() {
        MutableLiveData<String> liveData = findNavController().getCurrentBackStackEntry()
            .getSavedStateHandle()
            .getLiveData(CURRENCY);
        liveData.observe(getViewLifecycleOwner(),
            (Observer<Object>) coinItem -> {
                CurrencyItem item = (CurrencyItem) coinItem;
                viewModel.submitCurrency(item);
                binding.tvCurrency.setText(item.displayName());
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.startPeriodicUpdate();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.stopPeriodicUpdate();
    }

    @Override
    protected void setupView(View view) {
        binding.cvCrypto.setOnClickListener(view1 -> {
            Navigation.findNavController(view1)
                .navigate(PriceFragmentDirections.gotoCoinListFragment(
                    CurrencyType.COIN));
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
    protected void subscribeToViewModel(PriceViewModel viewModel) {
        viewModel.getUpdateLocalDataState().observe(this, dataUpdateState -> {
            switch (dataUpdateState.getStatus()) {
                case CREATED:
                case COMPLETE:
                    // to nothing
                    break;
                case SUCCESS:
                case ERROR:
                    // ignore result of data update
                    // if failed, then data will be fetched in next screen
                    hideProgress();
                    break;
                case LOADING:
                    showProgress();
                    break;
            }
        });

        viewModel.getPriceComparisonState().observe(this, priceItemUiState -> {
            switch (priceItemUiState.getStatus()) {
                case CREATED:
                case COMPLETE:
                    hideProgress();
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
