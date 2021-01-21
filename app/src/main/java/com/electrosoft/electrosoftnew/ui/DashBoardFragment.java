package com.electrosoft.electrosoftnew.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.FragmentDashBoardBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class DashBoardFragment extends Fragment {

    private static final String TAG = "DashBoardFragment";
    NavController navController;
    FragmentDashBoardBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();
    }


    private void actionViews() {

        binding.addRoomsCardView.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_dashboard_to_deviceConfigurationFragment);
        });

        binding.viewRoomsCardView.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_dashboard_to_nav_get_rooms);
        });


    }


}