package com.electrosoft.electrosoftnew.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.ActivityHomeLandingBinding;

public class HomeLandingActivity extends AppCompatActivity {

    NavController navController;
    ActivityHomeLandingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_landing);

        setSupportActionBar(binding.layout.toolbar);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.nav_dashboard, R.id.nav_get_rooms)
                        .setDrawerLayout(binding.drawerLayout)
                        .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }
}