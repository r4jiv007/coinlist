package com.digital.coinlist.ui.main;

import android.view.LayoutInflater;
import com.digital.coinlist.databinding.ActivityMainBinding;
import com.digital.coinlist.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected ActivityMainBinding getBinding(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    @Override
    protected void setupView() {

    }
}
