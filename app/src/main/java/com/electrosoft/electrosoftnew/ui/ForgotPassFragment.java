package com.electrosoft.electrosoftnew.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrosoft.electrosoftnew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassFragment extends Fragment {


    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_forgotpass, container, false );
    }

}
