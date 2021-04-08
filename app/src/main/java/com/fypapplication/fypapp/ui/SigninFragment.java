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
import com.fypapplication.fypapp.databinding.FragmentSigninBinding;

public class SigninFragment extends Fragment {


    NavController navController;
    FragmentSigninBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();


    }

    private void actionViews() {



        binding.loginBtn.setOnClickListener(v -> {

            navController.navigate(R.id.action_loginFragment_to_homeLandingActivity);

        });

        binding.forgotPassTv.setOnClickListener(v -> {

            navController.navigate(R.id.action_loginFragment_to_enterEmailFragment);
        });



    }

}