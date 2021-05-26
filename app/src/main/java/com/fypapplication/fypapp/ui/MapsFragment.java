package com.fypapplication.fypapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.MechServicesAdapter;
import com.fypapplication.fypapp.adapters.MechanicPriceAdapter;
import com.fypapplication.fypapp.databinding.DeleteRoomDialogBinding;
import com.fypapplication.fypapp.databinding.DialogueMechanicServiceBinding;
import com.fypapplication.fypapp.databinding.ItemMechServicesBinding;
import com.fypapplication.fypapp.databinding.MechanicAcceptedDialogBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.MechServices;
import com.fypapplication.fypapp.models.Services;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment implements MechanicPriceAdapter.MyServicesInterface {

    private static final String TAG = "MapsFragment";
    private static final int REQUEST_LOCATION = 1;
    ArrayList<User> userArrayList;
    ArrayList<MechServices> mechServicesArrayList;
    ArrayList<MechServices> priceArrayList;
    Context context;
    String mechId;
    MechanicPriceAdapter mechanicPriceAdapter;
    ItemMechServicesBinding binding;
    DialogueMechanicServiceBinding dialogueMechanicServiceBinding;
    Dialog dialog;
    String customerId;
    LocationManager locationManager;
    SharedPrefs sharedPrefs;
    String latitude, longitude;
    String date;
    MechanicPriceAdapter.MyServicesInterface myServicesInterface;
    MechServicesAdapter.MyServicesInterface anInterface;
    private List<Services> servicesArrayList = new ArrayList<>();
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onMapReady(GoogleMap googleMap) {

//            if (userArrayList.size() >0){
//                Log.d(TAG, "onMapReady: size is more than zero");
//                loadMarkers(googleMap);
//            }
            MapsFragmentArgs args = MapsFragmentArgs.fromBundle(getArguments());
            LatLng sydney;
            if (!args.getFromServices()) {
                sydney = new LatLng(Double.parseDouble(args.getLat()), Double.parseDouble(args.getLng()));

                customerId = args.getUserId();
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Emergency here!"));
                float zoom = 16.0f;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));

            } else {
                servicesArrayList = Arrays.asList(args.getService());
                getUserData(googleMap);
                date = args.getDate();
                sydney = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                float zoom = 16.0f;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));

            }

            googleMap.setOnMarkerClickListener(marker -> {


                if (args.getFromServices()) {
                    User user = (User) marker.getTag();
                    Log.d(TAG, "onMapReady: " + user.name);
                    mechId = user.id;
                    Log.d(TAG, "onMapReady: " + args.getFromServices());
                    Log.d(TAG, "onMapReady: size " + mechServicesArrayList.size());

                    priceArrayList.clear();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_SERVICE_BYMECH + user.id, null, res -> {

                        try {
                            JSONArray service = res.getJSONArray("Service");

                            for (int i = 0; i < service.length(); i++) {
                                MechServices mechServices = new MechServices();
                                JSONObject obj = service.getJSONObject(i);
                                mechServices.price = obj.optInt("price");
                                mechServices.service = obj.optString("service_name");
                                mechServices.vehicleType = obj.optString("vehicle_type");
                                mechServices.id = obj.optString("_id");

                                priceArrayList.add(mechServices);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        openPriceDialogue(user);


                    }, error -> {

                        Log.e(TAG, "onMapReady: ", error);

                    });

                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


                } else {

                    DeleteRoomDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.delete_room_dialog,
                            null, false);
                    final AlertDialog dialog = new AlertDialog.Builder(context)
                            .setView(binding1.getRoot())
                            .create();
                    binding1.text.setText("Do you want to accept the customer?");

                    if (dialog.getWindow() != null)
                        dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    binding1.cancelButton.setText("Cancel");
                    binding1.cancelButton.setOnClickListener(view -> {

                        dialog.dismiss();
                    });

                    binding1.confirmButton.setText("Accept");

                    binding1.confirmButton.setOnClickListener(v -> {

                        JSONObject params = new JSONObject();

                        try {

                            params.put("latitude", Double.parseDouble(args.getLat()));
                            params.put("longitude", Double.parseDouble(args.getLng()));
                            params.put("userId", args.getUserId());
                            params.put("mechanicId", sharedPrefs.getUser().id);
                            params.put("service", "Emergency");
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            params.put("time", dtf.format(now));

                            Log.d(TAG, "Bookings : " + params);

                        } catch (Exception e) {
                            Log.e(TAG, "apiSendTokensEmergency: ", e);
                        }


                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_GET_BOOKING, params,
                                response -> {

                                    Log.d(TAG, "mechanicAccepted: " + response);
                                    Toast.makeText(context, "Booking Created", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                    showCustomerDialog(args.getUserId());

                                }, error -> {

                            Log.e(TAG, "mechanicAccepted: ", error);

                        });


                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

                    });

                    dialog.show();

                }


                return false;
            });

        }
    };

    private void showCustomerDialog(String userId) {

        User user = new User();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_USERS, null,
                response -> {

                    //TODO yahan se we'll get users, filter out users with type mech (a number) and then add to arraylist

                    Log.d(TAG, "actionViews: " + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject userObj = response.getJSONObject(i);
                            Log.d(TAG, "actionViews: " + userObj.optInt("type"));

                            if (userObj.optString("_id").equals(userId)) {

                                user.email = userObj.optString("email");
                                user.phoneNumber = String.valueOf(userObj.optInt("phone"));
                                user.id = userObj.optString("_id");
                                user.lat = userObj.optString("latitude");
                                user.lng = userObj.optString("longitude");
                                user.name = userObj.optString("name");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    MechanicAcceptedDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.mechanic_accepted_dialog,
                            null, false);
                    final AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setView(binding1.getRoot())
                            .create();

                    if (dialog.getWindow() != null)
                        dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;

                    dialog.show();

                    binding1.tv.setText("Customer details");
                    binding1.setMechanic(user);
                    binding1.mechName.setText("Customer name: " + user.name);
                    binding1.mechNumber.setText("Phone number: " + user.phoneNumber);
                    binding1.callMech.setOnClickListener(view -> {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + user.phoneNumber));
                        startActivity(intent);
                    });

                    dialog.setCancelable(true);
                    dialog.show();

                }, error -> {

            Log.e(TAG, "showCustomerDialog: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }

    private void openPriceDialogue(User user) {


        DialogueMechanicServiceBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialogue_mechanic_service,
                null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding1.getRoot())
                .create();

        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;

        dialog.show();

        MechServicesAdapter adapter = new MechServicesAdapter(context, priceArrayList, anInterface, true);
        binding1.recyler.setLayoutManager(new LinearLayoutManager(context));
        binding1.recyler.setAdapter(adapter);


        dialog.setCancelable(true);
        dialog.show();

        binding1.callBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + user.phoneNumber));
            startActivity(intent);


        });

        binding1.bookNow.setOnClickListener(v -> {

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_DEVICE_TOKEN_BY_USER + user.id, null,
                    response -> {

                        JSONArray jsonArray = new JSONArray();


                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.optJSONObject(i);

                            jsonArray.put(obj.optString("device_token"));

                        }

                        apiSendTokensEmergency(jsonArray);

                        bookingCreated(user);


                    }, error -> {

                Log.e(TAG, "mechanicAccepted: ", error);

            });


            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

        });

//        dialogueMechanicServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialogue_mechanic_service, null, false);
//
//
//
//
//        Dialog dialog;
//        dialog = new Dialog(requireContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        dialog.setContentView(dialogueMechanicServiceBinding.getRoot());
//        dialog.setCancelable(true);
//
//        mechanicPriceAdapter = new MechanicPriceAdapter(context, priceArrayList, myServicesInterface);
//        dialogueMechanicServiceBinding.recyler.setLayoutManager(new LinearLayoutManager(context));
//        dialogueMechanicServiceBinding.recyler.setAdapter(mechanicPriceAdapter);
//
//        dialogueMechanicServiceBinding.callBtn.setOnClickListener(v -> {
//
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            intent.setData(Uri.parse("tel:" + user.phoneNumber));
//            startActivity(intent);
//
//        });
//
//        dialog.show();


    }

    private void bookingCreated(User user) {

        JSONObject params = new JSONObject();

        try {

            params.put("latitude", user.lat);
            params.put("longitude", user.lng);
            params.put("userId", sharedPrefs.getUser().id);
            params.put("mechanicId", user.id);
            params.put("service", servicesArrayList.toString());
            params.put("time", date);

            Log.d(TAG, "Bookings : " + params);

        } catch (Exception e) {
            Log.e(TAG, "apiSendTokensEmergency: ", e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_GET_BOOKING, params,
                response -> {

                    Log.d(TAG, "mechanicAccepted: " + response);
                    Toast.makeText(context, "Booking Created", Toast.LENGTH_SHORT).show();

                }, error -> {

            Log.e(TAG, "mechanicAccepted: ", error);

        });


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }

    private void apiSendTokensEmergency(JSONArray jsonArray) {

        JSONObject params = new JSONObject();

        try {
            params.put("title", "Booking request");

            JSONObject data = new JSONObject();

            data.put("lat", latitude);
            data.put("lng", longitude);
            data.put("user_id", sharedPrefs.getUser().id);
            data.put("time", date);
            JSONArray array = new JSONArray();

            for (int i = 0; i < servicesArrayList.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("service", servicesArrayList.get(i).service);
                obj.put("vehicleType", servicesArrayList.get(i).vehicleType);

                array.put(obj);
            }

            data.put("service", array.toString());


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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mechServicesArrayList = new ArrayList<>();
        priceArrayList = new ArrayList<>();
        sharedPrefs = new SharedPrefs(getContext());
        myServicesInterface = this;
        getLocation();
        Global.check = false;
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    private void getLocation() {
        Log.d(TAG, "getLocation: andr mt ao bahar jao besharam");
        locationManager = (LocationManager) requireActivity().getSystemService(getContext().LOCATION_SERVICE);

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        context = getContext();
        userArrayList = new ArrayList<>();
        actionViews();
//        getUserData();

    }

    private void actionViews() {

    }

    private void getUserData(GoogleMap googleMap) {

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_MECHS, null,
                response -> {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject userObj = response.getJSONObject(i);
                            Log.d(TAG, "actionViews: " + userObj.optInt("type"));

                            User user = new User();
                            user.email = userObj.optString("email");
                            user.phoneNumber = String.valueOf(userObj.optInt("phone"));
                            user.id = userObj.optString("_id");
                            user.lat = userObj.optString("latitude");
                            user.lng = userObj.optString("longitude");
                            user.name = userObj.optString("name");

                            Log.d(TAG, "getUserData: user name is " + user.name);
                            userArrayList.add(user);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (userArrayList.size() > 0) {
                        loadMarkers(googleMap);
                    }


                }, error -> {

            Log.e(TAG, "getUserData: ", error);

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/json");

                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    private void loadMarkers(GoogleMap googleMap) {


        for (int i = 0; i < userArrayList.size(); i++) {

            User user = userArrayList.get(i);
            LatLng latlng = new LatLng(Double.parseDouble(user.lat), Double.parseDouble(user.lng));

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(user.name));

            marker.setTag(user);

//            Marker marker = new MarkerOptions().position(latlng).title(user.id);
//            marker.setTag(user);
//            googleMap.addMarker(marker);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

            googleMap.getMaxZoomLevel();

        }
    }

    @Override
    public void onClickService(MechServices mechServices) {
        Log.d(TAG, "onClickService: " + mechServices.service);
    }
}