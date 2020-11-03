package com.electrosoft.electrosoftnew.ui;

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


public class DashBoardFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "DashBoardFragment";
    NavController navController;
    FragmentDashBoardBinding binding;
    BottomSheetDialog bottomSheetDialog;

    ConstraintLayout shareConstraintLayout, uploadConstraintLayout, copyConstraintLayout;


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

    @Override
    public void onClick(View view) {

        shareConstraintLayout.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        uploadConstraintLayout.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        copyConstraintLayout.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
    }

    private void actionViews() {




//
//        binding.designBottomSheet.setOnClickListener(v -> {
//            if (bottomSheetDialog == null) {
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet, null);
//                shareConstraintLayout = view.findViewById(R.id.shareConstraintLayout);
//                uploadConstraintLayout = view.findViewById(R.id.uploadConstraintLayout);
//                copyConstraintLayout = view.findViewById(R.id.copyConstraintLayout);
//
//                bottomSheetDialog = new BottomSheetDialog(getContext());
//                bottomSheetDialog.setContentView(view);
//
//                bottomSheetDialog.show();
//
//            }else
//            {
//                bottomSheetDialog.show();
//            }
//        });




        binding.addRoomsCardView.setOnClickListener(v -> {

            //    navController.navigate(R.id.action_nav_dashboard_to_addRoomFragment2);
            //    addRoomsDialog();

            navController.navigate(R.id.action_nav_dashboard_to_deviceConfigurationFragment);
        });

//        binding.notificationsCardView.setOnClickListener(v -> {
//
//        });

        binding.viewRoomsCardView.setOnClickListener(v -> {

                    navController.navigate(R.id.action_nav_dashboard_to_nav_get_rooms);




        });
    }


}