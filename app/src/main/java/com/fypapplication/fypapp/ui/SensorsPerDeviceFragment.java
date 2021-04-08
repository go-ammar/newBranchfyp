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
import com.fypapplication.fypapp.databinding.FragmentSensorsPerDeviceBinding;


public class SensorsPerDeviceFragment extends Fragment {

    private String URL;
    private static final String TAG = "AddRoomFragment";
    NavController navController;

    FragmentSensorsPerDeviceBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensors_per_device, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();
    }


    private void actionViews() {

    }


}
