package com.fypapplication.fypapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
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
import com.fypapplication.fypapp.adapters.RemoveMechAdapter;
import com.fypapplication.fypapp.databinding.DeleteRoomDialogBinding;
import com.fypapplication.fypapp.databinding.FragmentDashBoardBinding;
import com.fypapplication.fypapp.databinding.MechanicAcceptedDialogBinding;
import com.fypapplication.fypapp.helper.GPSTracker;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.Booking;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    String mechanicId;

    Handler handler;
    Runnable runnable;

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
        Log.d(TAG, "actionViews: " + extras);
        if (extras != null) {
            extras.getString("lat");
            extras.getString("lng");

            Log.d(TAG, "actionViews: lat " + extras.getString("lat"));

            DashBoardFragmentDirections.ActionNavDashboardToMapsFragment action =
                    DashBoardFragmentDirections.actionNavDashboardToMapsFragment();

            action.setLat(extras.getString("lat"));
            action.setLng(extras.getString("lng"));
            action.setUserId(extras.getString("user_id"));

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
            handler = new Handler();
            _apiSendEmergency();
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
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
            _apiForBooking();

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
        Log.d(TAG, "getLocation: andr mt ao bahar jao besharam");
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d(TAG, "getLocation: location gps status " + locationGPS);

            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Log.d(TAG, "getLocation:  " + latitude);
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
            data.put("user_id", sharedPrefs.getUser().id);

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


    public void mechanicAccepted() {
        User user = new User();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_USERS, null,
                response -> {


                    //TODO yahan se we'll get users, filter out users with type mech (a number) and then add to arraylist

                    Log.d(TAG, "mechanicAccepted: " + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject userObj = response.getJSONObject(i);
                            Log.d(TAG, "actionViews: " + userObj.optInt("type"));

                            if (userObj.optString("_id").equals(mechanicId)) {

                                user.email = userObj.optString("email");
                                user.phoneNumber = String.valueOf(userObj.optInt("phone"));
                                user.id = userObj.optString("_id");
                                user.lat = userObj.optString("latitude");
                                user.lng = userObj.optString("longitude");
                                user.name = userObj.optString("name");

                                openDialogForMech(user);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, error -> {

            Log.e(TAG, "mechanicAccepted: ", error);

        });


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void _apiForBooking() {

        handler.postDelayed(runnable = () -> {
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_BOOKING, null,
                    response -> {

                        ArrayList<Booking> bookingList = new ArrayList<>();
                        //TODO yahan se we'll get users, filter out users with type mech (a number) and then add to arraylist
//                    "_id": "609a4b66980dce0015d316e2",
//                            "time": "1970-01-19T18:12:04.443Z",
//                            "latitude": 5,
//                            "longitude": 7,
//                            "service": "Emergency",
//                            "userId": "6097f56c68d5f84a5841c27f",
//                            "mechanicId": "6098e8d6643e5d00157554e8",
//                            "createdAt": "2021-05-11T09:16:22.019Z",
//                            "updatedAt": "2021-05-11T09:16:22.019Z",
//                            "__v": 0

                        Log.d(TAG, "getUserData: response is  " + response.toString());
                        Log.d(TAG, "actionViews: " + response.length());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject userObj = response.getJSONObject(i);
                                Log.d(TAG, "actionViews: " + userObj.optString("_id"));

                                Booking user = new Booking();
                                user.id = userObj.optString("_id");
                                user.mechanicId = userObj.optString("mechanicId");
                                user.lat = userObj.optString("latitude");
                                user.lng = userObj.optString("longitude");
                                Log.d(TAG, "_bookingCreated: " + longitude + "  " + latitude);
                                if (user.lat.equals(latitude) && user.lng.equals(longitude)) {
                                    _bookingCreated(user.mechanicId);
                                } else {
                                    handler.postDelayed(runnable, 10000);
                                }
                                user.userId = userObj.optString("userId");


                                bookingList.add(user);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }, error -> {

                Log.e(TAG, "removeMech: ", error);

            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest);
        }, 10000);


    }

    private void _bookingCreated(String mechanicId) {
        handler = null;
        Log.d(TAG, "_bookingCreated: done booking");
    }

    private void openDialogForMech(User user) {
        MechanicAcceptedDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.mechanic_accepted_dialog,
                null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding1.getRoot())
                .create();

        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding1.callMech.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + user.phoneNumber));
            startActivity(intent);
        });

        dialog.show();

    }

}