package com.digital.coinlist.ui.base;

import android.app.AlertDialog;
import android.content.Context;
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
import com.digital.coinlist.R;
import com.google.android.material.snackbar.Snackbar;
import retrofit2.HttpException;

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

            viewModel = new ViewModelProvider(
                backStackEntry,
                getDefaultViewModelProviderFactory()).get(getViewModelClass()
            );
        } else {
            viewModel = new ViewModelProvider(this).get(getViewModelClass());
        }
        subscribeToViewModel(viewModel);
    }

    protected NavController findNavController() {
        return NavHostFragment.findNavController(this);
    }


    private AlertDialog progressDialog = null;

    protected void showProgress() {
        if (progressDialog == null) {
            progressDialog = createProgressLoading(requireContext());

        }
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    protected void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private AlertDialog createProgressLoading(Context context) {
        if (context == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        return builder.create();
    }

    protected void showError(Throwable error) {
        String msg = null;
        if (error instanceof HttpException) {
            msg = error.getMessage();
        }
        if(msg ==null){
            msg = getString(R.string.generic_error);
        }
        Snackbar snackbar = Snackbar
            .make(binding.getRoot(), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    protected void showError(String msg){
        Snackbar snackbar = Snackbar
            .make(binding.getRoot(), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}