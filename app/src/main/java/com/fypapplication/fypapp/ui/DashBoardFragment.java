package com.fypapplication.fypapp.ui;

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

import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentDashBoardBinding;
import com.fypapplication.fypapp.helper.Global;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


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

    }

    private void initFirebaseToken() {


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = Objects.requireNonNull(task.getResult()).getToken();
//                    sharedPrefs.putString("devicetoken",token);
//                    if (BuildConfig.DEBUG) Log.d(TAG, "initFirebaseToken: "+ token);
                    _apiUpdateFirebaseToken(token);
                });

    }

    public void _apiUpdateFirebaseToken(String token) {
//
//        JSONObject params = new JSONObject();
//
//        try {
//            if (BuildConfig.DEBUG) Log.d(TAG, "_apiUpdateFirebaseToken: "+token);
//            params.put("deviceToken", token);
//            params.put("deviceType", "Android");
//        } catch (Exception e) {
//            Log.e(TAG, "_apiUpdateFirebaseToken: ", e);
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Webservices.API_IS_LOGGED_IN, params, response -> {
//
//            if (BuildConfig.DEBUG) Log.d(TAG, "_apiUpdateFirebaseToken: " + response);
//
//        }, error -> {
//
//            try {
//
//                if (error instanceof TimeoutError) {
//                    global.showGenericErrorDialog(requireContext(), getString(R.string.error_internet));
//                } else {
//                    try {
//
//
//                        global.showGenericErrorDialog(requireContext(), global.parseError(error.networkResponse.statusCode, error.networkResponse.data,requireContext()));
//
//
//                    } catch (Exception e) {
//                        Log.e(TAG, "_apiUpdateFirebaseToken: ", e);
//                    }
//
//                }
//
//
//            } catch (Exception e) {
//                if (BuildConfig.DEBUG) Log.d(TAG, "_apiUpdateFirebaseToken: ", e);
//            }
//
//
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "Bearer " + user.token);
//
//
//                return headers;
//            }
//        };
//
//
//        VolleySingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest);
//
    }
//

}