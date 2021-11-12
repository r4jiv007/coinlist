package com.digital.coinlist.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<T extends ViewBinding, V extends BaseViewModel> extends
    Fragment {

    protected abstract T getBinding(LayoutInflater inflater);

    protected abstract Class<V> getViewModelClass();

    protected abstract void setupView(View view);

    protected abstract void subscribeToViewModel(V viewModel);

    protected V viewModel;
    protected T binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        createViewModel();
        binding = getBinding(getLayoutInflater());
        View view = binding.getRoot();
        setupView(view);
        return view;
    }

    private void createViewModel() {
        if (viewModel != null) {
            return;
        }
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
        subscribeToViewModel(viewModel);
    }
}