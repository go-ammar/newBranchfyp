package com.fypapplication.fypapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.RemoveMechAdapter;
import com.fypapplication.fypapp.models.MechServices;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private static final String TAG = "MapsFragment";
    ArrayList<User> userArrayList;
    ArrayList<MechServices> mechServicesArrayList;
    Context context;
    String MechId;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {

//            if (userArrayList.size() >0){
//                Log.d(TAG, "onMapReady: size is more than zero");
//                loadMarkers(googleMap);
//            }
            MapsFragmentArgs args = MapsFragmentArgs.fromBundle(getArguments());
            LatLng sydney;
            if (args != null) {
                sydney = new LatLng(Double.parseDouble(args.getLat()), Double.parseDouble(args.getLng()));

                googleMap.addMarker(new MarkerOptions().position(sydney).title("Emergency here!"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                googleMap.getMaxZoomLevel();
            } else {
//                sydney = new LatLng(-34, 151);
                getUserData(googleMap);
            }

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    MechId = marker.getId();
                    if (MechId != null){
                        //get service here
                        getMechServices();
                    }
                    return false;
                }
            });


        }
    };

    private void getMechServices() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_SERVICES, null, res -> {

            for (int i = 0; i<res.length(); i++){
                try {
                    JSONObject object = res.getJSONObject(i);

                    MechServices mechServices = new MechServices();
                    if (object.getString("mechanic").equals(MechId)){
                        mechServices.service = object.getString("service_name");
                        mechServices.vehicleType = object.getString("vehicle_type");
                        mechServices.price = object.getInt("price");

                        mechServicesArrayList.add(mechServices);

                        if (mechServicesArrayList.size() >0){
                            //attach adapter here// with notify on cliock
                        }

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "getServices: ", e);
                }

            }


        }, error -> {

            Log.e(TAG, "getServices: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
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
        mechServicesArrayList = new ArrayList<>();
//        getUserData();

    }

    private void getUserData(GoogleMap googleMap) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_USERS, null,
                response -> {


                    //TODO yahan se we'll get users, filter out users with type mech (a number) and then add to arraylist


                    Log.d(TAG, "getUserData: response is  "+response.toString());
                    Log.d(TAG, "actionViews: " + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject userObj = response.getJSONObject(i);
                            Log.d(TAG, "actionViews: "+userObj.optInt("type"));

                            if (userObj.optInt("type") == 3) {
                                User user = new User();
                                user.email = userObj.optString("email");
                                user.phoneNumber = String.valueOf(userObj.optInt("phone"));
                                user.id = userObj.optString("_id");
                                user.lat = userObj.optString("latitude");
                                user.lng = userObj.optString("longitude");
                                user.name = userObj.optString("name");

                                Log.d(TAG, "getUserData: user name is "+user.name);
                                userArrayList.add(user);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (userArrayList.size() >0){
                        loadMarkers(googleMap);
                    }


                }, error -> {

            Log.e(TAG, "removeMech: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    private void loadMarkers(GoogleMap googleMap) {


        for (int i = 0; i < userArrayList.size() ; i++){
            User user = new User();
            LatLng latlng = new LatLng(Double.parseDouble(user.lat), Double.parseDouble(user.lng));
            googleMap.addMarker(new MarkerOptions().position(latlng).title(user.name));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            googleMap.getMaxZoomLevel();
        }
    }
}