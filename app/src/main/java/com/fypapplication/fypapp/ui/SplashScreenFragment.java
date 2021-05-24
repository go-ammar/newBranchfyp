package com.fypapplication.fypapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.google.firebase.FirebaseApp;

public class SplashScreenFragment extends Fragment {


    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }


    // CTRL + O

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        FirebaseApp.initializeApp(getContext());

        actionViews();
    }

    void actionViews() {

        Handler handler = new Handler();

        SharedPrefs sharedPrefs = new SharedPrefs(requireContext());

        if (sharedPrefs.getKey() == null) {
            handler.postDelayed(() -> navController.navigate(R.id.action_splashScreen_to_loginFragment), 1000);
        } else {
            handler.postDelayed(() -> navController.navigate(R.id.action_splashScreen_to_homeLandingActivity), 1000);
        }
    }


}
