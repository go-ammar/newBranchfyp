package com.fypapplication.fypapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentDashBoardBinding;
import com.fypapplication.fypapp.databinding.LayoutYouHaveABookingBinding;
import com.fypapplication.fypapp.databinding.MechanicAcceptedDialogBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.Booking;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


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
    private Location mLocation;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false);
        Log.d(TAG, "onCreateView: ");
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedPrefs = new SharedPrefs(getContext());
        mLocationListener = new LocationListener(LocationManager.GPS_PROVIDER);

        try {
            setViews();
        } catch (Exception e) {
            Log.e(TAG, "onViewCreated: ", e);
        }
        actionViews();

    }


    private void setViews() {

        FirebaseApp.initializeApp(getContext());


        if (sharedPrefs.getUser().type == Global.ADMIN_TYPE) {
            Log.d(TAG, "setViews: admin");
            binding.adminConstraint.setVisibility(View.VISIBLE);
        } else if (sharedPrefs.getUser().type == Global.CUSTOMER_TYPE) {
            Log.d(TAG, "setViews: customer");
            binding.customerConstraint.setVisibility(View.VISIBLE);
        } else if (sharedPrefs.getUser().type == Global.MECH_TYPE) {
            Log.d(TAG, "setViews: mech");
            initFirebaseToken();
            binding.mechConstraint.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void actionViews() {

        initializeLocationManager();
        startTracking();
        locationManager = (LocationManager) requireActivity().getSystemService(getContext().LOCATION_SERVICE);
//        getLocation();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Log.d(TAG, "actionViews: time " + dtf.format(now));

        Bundle extras = getActivity().getIntent().getExtras();
        Log.d(TAG, "actionViews: " + extras);
        if (Global.check)
            if (extras != null) {
                extras.getString("lat");
                extras.getString("lng");

                Log.d(TAG, "actionViews: check "+extras.getString("time"));
                if (!extras.getString("time").equals("")) {
                    openDialogForNewBooking(extras.getString("user_id"));
                } else {

                    Log.d(TAG, "actionViews: lat " + extras.getString("lat"));

                    DashBoardFragmentDirections.ActionNavDashboardToMapsFragment action =
                            DashBoardFragmentDirections.actionNavDashboardToMapsFragment();

                    action.setLat(extras.getString("lat"));
                    action.setLng(extras.getString("lng"));
                    action.setUserId(extras.getString("user_id"));

                    navController.navigate(action);
                }
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

    private void openDialogForNewBooking(String id) {

        LayoutYouHaveABookingBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_you_have_a_booking,
                null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding1.getRoot())
                .create();

        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_USERS + "/" + id, null, response -> {

            User user = new User();
            Log.d(TAG, "openDialogForNewBooking: "+response);

            user.phoneNumber = response.optString("phone");
            user.id = response.optString("_id");
            user.name = response.optString("name");
            user.type = response.optString("type");
            user.email = response.optString("email");
            user.lat = response.optString("latitude");
            user.lng = response.optString("longitude");

            binding1.setMechanic(user);
//            binding1.mechName.setText(response.optString("name"));
//            binding1.mechNumber.setText(response.optString("phone"));

            dialog.show();


            binding1.callMech.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + user.phoneNumber));
                startActivity(intent);
            });

            dialog.setCancelable(true);
            dialog.show();


        }, error -> {

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

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void _apiSendEmergency() {

        JSONArray jsonArray = new JSONArray();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebServices.API_GET_NOTIFCATION_PAYLOAD, null, response -> {


            Log.d(TAG, "_apiSendEmergency: " + response);
            JSONArray array = response.optJSONArray("tokens");

            for (int i = 0; i < array.length(); i++) {
                try {

                    JSONObject obj = array.getJSONObject(i);
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
                Log.d(TAG, "getLocation: current lat  " + latitude);
                Log.d(TAG, "getLocation: current lng " + longitude);
            }
        }
    }

    private void apiSendTokensEmergency(JSONArray jsonArray) {


        JSONObject params = new JSONObject();

        try {
            params.put("title", "Emergency nearby");

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


//        FirebaseApp.initializeApp(getContext());
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
//            params.put("user_id", sharedPrefs.getUser().id);
//            params.put("deviceType", "Android");
            Log.d(TAG, "_apiUpdateFirebaseToken params: ");
        } catch (Exception e) {
            Log.e(TAG, "_apiUpdateFirebaseToken params: ", e);
        }

        String id = sharedPrefs.getUser().id;

        Log.d(TAG, "_apiUpdateFirebaseToken: " + params);
        try {
            Log.d(TAG, "_apiUpdateFirebaseToken: api hit is " + WebServices.API_IS_LOGGED_IN + "/" + id);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                    WebServices.API_IS_LOGGED_IN + "/" + id, params, response -> {

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
                                    if (runnable != null)
                                        _bookingCreated(user.mechanicId);
                                } else {
                                    try {
                                        handler.postDelayed(runnable, 10000);
                                    } catch (Exception e) {
                                        Log.e(TAG, "_apiForBooking: ", e);
                                    }
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
        runnable = null;
        this.mechanicId = mechanicId;
        mechanicAccepted();
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

        dialog.show();

        binding1.setMechanic(user);
        binding1.callMech.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + user.phoneNumber));
            startActivity(intent);
        });

        dialog.setCancelable(true);
        dialog.show();

    }

    private void initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void startTracking() {

        try {


            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 15, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 15, mLocationListener);

        } catch (SecurityException ex) {
            // Log.i(TAG, "fail to request location update, ignore", ex);
        }
    }


    private class LocationListener implements android.location.LocationListener {
        private final String TAG = "LocationListener";

        public LocationListener(String provider) {
            mLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            mLocation = location;

            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

            Log.d(TAG, "getLocation: current lat  " + latitude);
            Log.d(TAG, "getLocation: current lng " + longitude);

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + status);
        }

    }

}