package com.digital.coinlist.ui.main;

import android.view.LayoutInflater;
import androidx.appcompat.app.ActionBar;
import com.digital.coinlist.databinding.ActivityMainBinding;
import com.digital.coinlist.ui.base.BaseActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected ActivityMainBinding getBinding(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    @Override
    protected void setupView() {
        ActionBar actionBar = getSupportActionBar();

    }
}
