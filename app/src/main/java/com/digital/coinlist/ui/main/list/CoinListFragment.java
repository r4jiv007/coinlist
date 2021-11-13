package com.digital.coinlist.ui.main.list;

import static com.digital.coinlist.ui.main.price.PriceFragment.COIN;
import static com.digital.coinlist.ui.main.price.PriceFragment.CURRENCY;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.digital.coinlist.R;
import com.digital.coinlist.databinding.FragmentCoinListBinding;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.ui.base.BaseFragment;
import com.digital.coinlist.ui.main.adapter.CoinListAdapter;
import com.digital.coinlist.ui.main.adapter.OnItemSelectionListener;
import com.digital.coinlist.ui.main.adapter.Selectable;
import com.digital.coinlist.util.rx.SchedulerProvider;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.ArrayList;
import javax.inject.Inject;

@AndroidEntryPoint
public class CoinListFragment extends
    BaseFragment<FragmentCoinListBinding, CoinListViewModel> implements
    OnItemSelectionListener {

    private CoinListAdapter listAdapter;

    @Inject
    SchedulerProvider schedulerProvider;


    private final TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            viewModel
                .searchItem(charSequence.toString(), CoinListFragmentArgs.fromBundle(getArguments())
                    .getCurrencnyType());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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
        setTitle();
        initRecyclerView();
    }

    private void setTitle() {
        int titleId = -1;
        CurrencyType currencyType = CoinListFragmentArgs.fromBundle(getArguments())
            .getCurrencnyType();
        if (currencyType == CurrencyType.COIN) {
            titleId = R.string.select_crypto;
        } else {
            titleId = R.string.select_currency;
        }
        if (getActivity() != null) {
            getActivity().setTitle(titleId);
        }
    }

    private void loadData() {
        CurrencyType currencyType = CoinListFragmentArgs.fromBundle(getArguments())
            .getCurrencnyType();
        viewModel.loadItemList(currencyType);
    }

    private void initRecyclerView() {
        listAdapter = new CoinListAdapter(new ArrayList<>(), this);
        binding.rcvCoinList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvCoinList.setAdapter(listAdapter);
    }

    private void addTextWatcher() {
        binding.etItemSearch.addTextChangedListener(searchTextWatcher);
    }

    private void removeTextWatcher() {
        binding.etItemSearch.removeTextChangedListener(searchTextWatcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        addTextWatcher();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeTextWatcher();
    }

    @Override
    protected void subscribeToViewModel(CoinListViewModel viewModel) {
        viewModel.getCoinListData().observe(this, coinListUiState -> {
            switch (coinListUiState.getStatus()) {
                case CREATED:
                case COMPLETE:
                    break;
                case LOADING:
                    binding.rcvCoinList.setVisibility(View.GONE);
                    showProgress();
                    break;
                case SUCCESS:
                    binding.rcvCoinList.setVisibility(View.VISIBLE);
                    hideProgress();
                    listAdapter.swapData(coinListUiState.getData());
                    break;
                case ERROR:
                    handleError();
                    break;
            }
        });
        loadData();
    }

    @Override
    public void onItemSelected(Selectable item) {
        CurrencyType currencyType = CoinListFragmentArgs.fromBundle(getArguments())
            .getCurrencnyType();
        if (findNavController().getPreviousBackStackEntry() == null) {
            return;
        }
        if (currencyType == CurrencyType.COIN) {
            findNavController().getPreviousBackStackEntry().getSavedStateHandle()
                .set(COIN, (CoinItem) item);
        } else {
            findNavController().getPreviousBackStackEntry().getSavedStateHandle()
                .set(CURRENCY, (CurrencyItem) item);
        }
        viewModel.submitSelectableItem(currencyType, item);
        findNavController().popBackStack();
    }

    private void handleError() {
        hideProgress();
        binding.rcvCoinList.setVisibility(View.GONE);
        CurrencyType currencyType = CoinListFragmentArgs.fromBundle(getArguments())
            .getCurrencnyType();
        if (currencyType == CurrencyType.COIN) {
            showError(getString(R.string.error_coin_list));
        } else {
            showError(getString(R.string.error_currency_list));
        }
    }
}
