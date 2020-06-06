package com.electrosoft.electrosoftnew.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.ActivityHomeLandingBinding;

public class HomeLandingActivity extends AppCompatActivity {

    NavController navController;
    ActivityHomeLandingBinding binding;
    private String EXTRA_LOGOUT = "clearBackStack";
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra(EXTRA_LOGOUT, false)) {
            clearBackStack();
        } else {
            binding = DataBindingUtil.setContentView(this,R.layout.activity_home_landing);
            setUpNavigation();
        }

    }

    private void setUpNavigation(){

        setSupportActionBar(binding.layout.toolbar);


         appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.nav_dashboard, R.id.nav_notifications,R.id.nav_contact,R.id.nav_about)
                        .setDrawerLayout(binding.drawerLayout)
                        .build();

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void clearBackStack() {

        Intent intent = new Intent(this, HomeLandingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();

    }

}