package com.electrosoft.electrosoftnew.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private AppBarConfiguration mAppBarConfiguration;

    FragmentHomeBinding binding;

    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false );

        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        actionViews();
    }

    private void actionViews(){

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_dashboard, R.id.nav_get_rooms)
//                .setDrawerLayout(binding.drawerLayout)
//                .build();
//
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(requireActivity(), navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navigationView, navController);

    }


}
