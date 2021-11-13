package com.digital.coinlist.ui.main.list;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.digital.coinlist.R;
import com.digital.coinlist.databinding.FragmentCoinListBinding;
import com.digital.coinlist.ui.base.BaseFragment;
import com.digital.coinlist.ui.main.CoinViewModel;
import com.digital.coinlist.ui.main.adapter.CoinListAdapter;
import com.digital.coinlist.ui.main.adapter.OnItemSelectionListener;
import com.digital.coinlist.ui.main.adapter.Selectable;
import com.digital.coinlist.util.rx.SchedulerProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@AndroidEntryPoint
public class CoinListFragment extends
    BaseFragment<FragmentCoinListBinding, CoinViewModel> implements
    OnItemSelectionListener {

    private CoinListAdapter listAdapter;
    private Disposable disposable;

    @Inject
    SchedulerProvider schedulerProvider;

    private PublishSubject<String> searchSubjet = PublishSubject.create();
    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchSubjet.onNext(charSequence.toString());
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

    private void setUpSearch() {
        disposable = searchSubjet
            .debounce(300L, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(search -> listAdapter.getFilter().filter(search));
    }

    private void disposeSearch() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
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
        setUpSearch();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeTextWatcher();
        disposeSearch();
    }

    @Override
    protected void subscribeToViewModel(CoinViewModel viewModel) {
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
                    hideProgress();
                    binding.rcvCoinList.setVisibility(View.GONE);
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
