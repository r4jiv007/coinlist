package com.digital.coinlist.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<T extends ViewBinding> extends Activity {

    protected abstract T getBinding(LayoutInflater inflater);

    protected abstract void setupView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getBinding(getLayoutInflater()).getRoot());
        setupView();
    }
}