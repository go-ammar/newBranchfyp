package com.fypapplication.fypapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentDashBoardBinding;
import com.fypapplication.fypapp.helper.Global;


public class DashBoardFragment extends Fragment {

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
        binding.carMechanicCard.setOnClickListener(v -> {
            DashBoardFragmentDirections.ActionNavDashboardToServicesFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToServicesFragment();

            action.setVehicleType(Global.VEHICLE_CAR);

            Navigation.createNavigateOnClickListener(action).onClick(binding.carMechanicCard);
        });

        binding.autoMechanic.setOnClickListener(v -> {
            DashBoardFragmentDirections.ActionNavDashboardToServicesFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToServicesFragment();

            action.setVehicleType(Global.VEHICLE_RICKSHAW);

            Navigation.createNavigateOnClickListener(action).onClick(binding.autoMechanic);
        });

        binding.bikeMechanic.setOnClickListener(v -> {
            DashBoardFragmentDirections.ActionNavDashboardToServicesFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToServicesFragment();

            action.setVehicleType(Global.VEHICLE_BIKE);

            Navigation.createNavigateOnClickListener(action).onClick(binding.bikeMechanic);
        });

        binding.truckMechanic.setOnClickListener(v -> {
            DashBoardFragmentDirections.ActionNavDashboardToServicesFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToServicesFragment();

            action.setVehicleType(Global.VEHICLE_TRUCK);

            Navigation.createNavigateOnClickListener(action).onClick(binding.truckMechanic);
        });

        binding.myAccount.setOnClickListener(v -> {
            DashBoardFragmentDirections.ActionNavDashboardToMyAccountFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToMyAccountFragment();

            Navigation.createNavigateOnClickListener(action).onClick(binding.myAccount);

        });

        binding.myServices.setOnClickListener(v -> {

            DashBoardFragmentDirections.ActionNavDashboardToMyServicesFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToMyServicesFragment();

            Navigation.createNavigateOnClickListener(action).onClick(binding.myServices);
        });


        binding.addMechs.setOnClickListener(v -> {
            DashBoardFragmentDirections.ActionNavDashboardToAddMechsFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToAddMechsFragment();

            Navigation.createNavigateOnClickListener(action).onClick(binding.addMechs);

        });

        binding.removeMechs.setOnClickListener(v -> {
            DashBoardFragmentDirections.ActionNavDashboardToRemoveMechsFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToRemoveMechsFragment();

            Navigation.createNavigateOnClickListener(action).onClick(binding.removeMechs);

        });
    }


}