package com.digital.coinlist.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;

public abstract class BaseFragment<T extends ViewBinding, V extends BaseViewModel> extends
    Fragment {

    protected abstract T getBinding(LayoutInflater inflater);

    protected abstract Class<V> getViewModelClass();

    protected abstract boolean createSharedViewModel();

    protected abstract int navGraphIdForViewModel();

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
        if (createSharedViewModel()) {
            NavBackStackEntry backStackEntry = findNavController()
                .getBackStackEntry(navGraphIdForViewModel());

            viewModel =  new ViewModelProvider(
                backStackEntry,
                getDefaultViewModelProviderFactory()).get(getViewModelClass()
            );
//             viewModel=   new ViewModelProvider(backStackEntry).get(getViewModelClass());
        } else {
            viewModel = new ViewModelProvider(this).get(getViewModelClass());
        }
        subscribeToViewModel(viewModel);
    }

    protected NavController findNavController() {
        return NavHostFragment.findNavController(this);
    }
}