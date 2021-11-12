package com.digital.coinlist.ui.main.list;

import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.digital.coinlist.R;
import com.digital.coinlist.databinding.FragmentCoinListBinding;
import com.digital.coinlist.ui.base.BaseFragment;
import com.digital.coinlist.ui.main.CoinViewModel;
import com.digital.coinlist.ui.main.adapter.CoinListAdapter;
import com.digital.coinlist.ui.main.adapter.ItemSelectionListener;
import com.digital.coinlist.ui.main.adapter.Selectable;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.ArrayList;

@AndroidEntryPoint
public class CoinListFragment extends
    BaseFragment<FragmentCoinListBinding, CoinViewModel> implements
    ItemSelectionListener {

    private CoinListAdapter listAdapter;

    @Override
    protected FragmentCoinListBinding getBinding(LayoutInflater inflater) {
        return FragmentCoinListBinding.inflate(inflater);
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
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    protected void subscribeToViewModel(CoinViewModel viewModel) {
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

        loadData();
    }

    @Override
    public void onItemSelected(Selectable item) {
        CurrencyType currencyType = CoinListFragmentArgs.fromBundle(getArguments())
            .getCurrencnyType();
        viewModel.submitSelectableItem(currencyType, item);
        findNavController().popBackStack();
    }
}
