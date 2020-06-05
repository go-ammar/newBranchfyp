package com.electrosoft.electrosoftnew.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrosoft.electrosoftnew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetRoomsFragment extends Fragment {


    public GetRoomsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_get_rooms, container, false );
    }

}
