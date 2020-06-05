package com.electrosoft.electrosoftnew.ui;

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
import android.widget.Toast;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {


    private static final String TAG = "LoginFragment";
    NavController navController;

    FragmentLoginBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        return binding.getRoot();


    }
    // CTRL + o

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();
    }


    private void actionViews(){


        binding.loginBtn.setOnClickListener(v -> {
//            if(validations())
//            Toast.makeText(requireContext(), "Login btn clicked", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.action_loginFragment_to_homeLandingActivity);

        });

    }

    private boolean validations(){

        if(binding.emailEt.getText()!=null && !binding.emailEt.getText().toString().isEmpty()){
        if(binding.passwordEt.getText()!=null && !binding.passwordEt.getText().toString().isEmpty()) {
            return true;
        }else
            binding.textInputLayout.setError(getString(R.string.error_empty_fields));

        }else
            binding.textEmail.setError(getString(R.string.error_empty_fields));

        return false;
    }

}
