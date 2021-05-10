package com.fypapplication.fypapp.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentAddMechsBinding;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddMechsFragment extends Fragment {


    private static final String TAG = "AddMechsFragment";
    Context context;
    FragmentAddMechsBinding binding;
    private String latitude, longitude;
    private String address, city, country;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_mechs, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        context = getContext();
        actionViews();
    }

    private void actionViews() {

        binding.locationET.setOnClickListener(v -> {
            setUpGoogleClient();
            Log.d(TAG, "actionViews: ");
        });

        binding.addServiceBtn.setOnClickListener(v -> {

            if (!binding.locationET.getText().toString().isEmpty() && !binding.nameET.getText().toString().isEmpty() &&
                    !binding.emailET.getText().toString().isEmpty() && !binding.phoneET.getText().toString().isEmpty()) {
                apiAddMech();
            }

        });
    }

    private void apiAddMech() {
        JSONObject params = new JSONObject();

        try {
            // BODY

            params.put("email", binding.emailET.getText().toString());
            params.put("password", binding.passwordET.getText().toString());
            params.put("latitude", String.valueOf(latitude));
            params.put("longitude", String.valueOf(longitude));
            params.put("name", binding.nameET.getText().toString());
            params.put("type", 3);
            params.put("phone", binding.phoneET.getText().toString());
            Log.d(TAG, "apiAddMech: " + params);
        } catch (Exception e) {
            Log.e(TAG, "_apiLogin: ", e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_SIGNUP, params, res -> {

            Log.d(TAG, "_apiLogin: res " + res);

            Toast.makeText(context, "Mechanic Added", Toast.LENGTH_SHORT).show();


        }, error -> {

            Log.e(TAG, "apiAddMech: ", error);

        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void setUpGoogleClient() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {

            Intent intent = builder.build(getActivity());
            startActivityForResult(intent, 0);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0) {
            try {


                if (data != null) {

                    Place place = PlacePicker.getPlace(context, data);
                    LatLng latLng = place.getLatLng();

                    CharSequence placeName = place.getName();
                    String title = placeName.toString();

                    Log.d(TAG, "onActivityResult: " + place.getAddress());
                    binding.locationET.setText(place.getAddress());
//                    address = String.valueOf(place.getAddress());
                    latitude = String.valueOf(latLng.latitude);
                    longitude = String.valueOf(latLng.longitude);

                    Log.d(TAG, "onActivityResult: " + latitude);

                    getAddress(latLng.latitude, latLng.longitude);

                }


            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    private void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            Log.v(TAG, "Address" + add);
            binding.locationET.setText(add);

            city = obj.getLocality();
            country = obj.getCountryName();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}