package com.fypapplication.fypapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentDashBoardBinding;
import com.fypapplication.fypapp.helper.GPSTracker;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fypapplication.fypapp.sharedprefs.SharedPrefs;


public class DashBoardFragment extends Fragment {

    private static final String TAG = "DashBoardFragment";
    private static final int REQUEST_LOCATION = 1;
    NavController navController;
    FragmentDashBoardBinding binding;
    SharedPrefs sharedPrefs;
    LocationManager locationManager;
    String latitude, longitude;

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
        FirebaseApp.initializeApp(requireContext());
        navController = Navigation.findNavController(view);
        sharedPrefs = new SharedPrefs(getContext());

        setViews();
        actionViews();
    }

    private void setViews() {
        if (sharedPrefs.getUser().type == Global.ADMIN_TYPE) {
            binding.adminConstraint.setVisibility(View.VISIBLE);
        } else if (sharedPrefs.getUser().type == Global.CUSTOMER_TYPE) {
            initFirebaseToken();
            binding.customerConstraint.setVisibility(View.VISIBLE);
        } else if (sharedPrefs.getUser().type == Global.MECH_TYPE) {
            initFirebaseToken();
            binding.mechConstraint.setVisibility(View.VISIBLE);
        }
    }


    private void actionViews() {
        locationManager = (LocationManager) requireActivity().getSystemService(getContext().LOCATION_SERVICE);
        getLocation();

        Bundle extras = getActivity().getIntent().getExtras();
        Log.d(TAG, "actionViews: "+extras);
        if (extras != null) {
            extras.getString("lat");
            extras.getString("lng");

            Log.d(TAG, "actionViews: lat "+extras.getString("lat"));

            DashBoardFragmentDirections.ActionNavDashboardToMapsFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToMapsFragment();

            action.setLat(extras.getString("lat"));
            action.setLng(extras.getString("lng"));

            navController.navigate(action);
        }

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

        binding.emergencyBtn.setOnClickListener(v -> {
//            navController.navigate(R.id.action_nav_dashboard_to_mapsFragment);
            _apiSendEmergency();
        });
    }

    private void _apiSendEmergency() {

//        GPSTracker gpsTracker = new GPSTracker(getContext());

//        if (gpsTracker.getIsGPSTrackingEnabled()) {
//            stringLatitude = String.valueOf(gpsTracker.latitude);

//            stringLongitude = String.valueOf(gpsTracker.longitude);

//        }

        JSONArray jsonArray = new JSONArray();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                WebServices.API_GET_NOTIFCATION_PAYLOAD, null, response -> {


            Log.d(TAG, "_apiSendEmergency: " + response);

            for (int i = 0; i < response.length(); i++) {
                try {

                    JSONObject obj = response.getJSONObject(i);
                    Log.d(TAG, "Inside Loop: " + i);
                    jsonArray.put(obj.getString("device_token"));

                } catch (JSONException e) {
                    Log.e(TAG, "_apiSendEmergency: ", e);
                }
            }

            apiSendTokensEmergency(jsonArray);

        }, error -> {

            Log.e(TAG, "_apiSendEmergency: ", error);

        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/json");

                return headers;
            }
        };


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
            } else {
            }
        }
    }

    private void apiSendTokensEmergency(JSONArray jsonArray) {


        JSONObject params = new JSONObject();


        try {
            params.put("title", "title of notification");

            JSONObject data = new JSONObject();

            data.put("lat", latitude);
            data.put("lng", longitude);

            params.put("body", data.toString());
            params.put("tokens", jsonArray);

            Log.d(TAG, "apiSendTokensEmergency: " + params);

        } catch (Exception e) {
            Log.e(TAG, "apiSendTokensEmergency: ", e);
        }

        try {
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST,
                    WebServices.API_IS_LOGGED_IN + "/send-notification", params, res -> {


            }, error -> {

                Log.e(TAG, "_apiSendEmergency: ", error);

            }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();

                    headers.put("Content-Type", "application/json");

                    return headers;
                }
            };


            jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest1);

        } catch (Exception e) {
            Log.e(TAG, "apiSendTokensEmergency: ", e);
        }
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
                    Log.d(TAG, "initFirebaseToken: " + token);
                    _apiUpdateFirebaseToken(token);
                });

    }

    public void _apiUpdateFirebaseToken(String token) {

        JSONObject params = new JSONObject();

        try {
            params.put("device_token", token);
            params.put("user_id", sharedPrefs.getUser().id);
//            params.put("deviceType", "Android");
            Log.d(TAG, "_apiUpdateFirebaseToken params: ");
        } catch (Exception e) {
            Log.e(TAG, "_apiUpdateFirebaseToken params: ", e);
        }

        Log.d(TAG, "_apiUpdateFirebaseToken: " + params);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    WebServices.API_IS_LOGGED_IN + "/register-device", params, response -> {

                Log.d(TAG, "_apiUpdateFirebaseToken api: " + response);

            }, error -> {

                Log.e(TAG, "_apiUpdateFirebaseToken api: ", error);

            }) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();

                    headers.put("Content-Type", "application/json");

                    return headers;
                }
            };


            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            Log.e(TAG, "_apiUpdateFirebaseToken: ", e);
        }
    }
//

}