package com.electrosoft.electrosoftnew.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.FragmentDashBoardBinding;
import com.electrosoft.electrosoftnew.databinding.FragmentLoginBinding;
import com.electrosoft.electrosoftnew.sharedprefs.SharedPrefs;


public class DashBoardFragment extends Fragment {

    private static final String TAG = "DashBoardFragment";
    private String URL;
    NavController navController;

    FragmentDashBoardBinding binding;
    SharedPrefs sharedPrefs;

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


    private void actionViews(){


        //sharedPrefs = new SharedPrefs(requireContext());
        //sharedPrefs.putString("Sample","Sample");
         //String a = sharedPrefs.getStrings( "Sample" );

        //Log.d(TAG, "actionViews: "+sharedPrefs.getStrings("Sample"));

        binding.addRoomsCardView.setOnClickListener(v ->  {

            navController.navigate(R.id.action_nav_dashboard_to_addRoomFragment2);
        });

        binding.notificationsCardView.setOnClickListener(v ->  {

        });

        binding.viewRoomsCardView.setOnClickListener(v ->  {

           navController.navigate(R.id.action_nav_dashboard_to_nav_get_rooms);

//            NotificationFragmentDirections.ActionNotificationFragmentNavToNotificationDetailsFragment action = NotificationFragmentDirections.actionNotificationFragmentNavToNotificationDetailsFragment("notification");
//            navController.navigate(action);



            // GET IN OTHER FRAGMENT
//            assert getArguments() != null;
//            String testValue= GetRoomsFragmentArgs.fromBundle(getArguments()).getTestValue();

        });


    }
}