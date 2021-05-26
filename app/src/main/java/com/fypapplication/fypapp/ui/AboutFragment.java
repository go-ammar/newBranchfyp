package com.fypapplication.fypapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentAboutBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.Booking;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AboutFragment extends Fragment {

    FragmentAboutBinding binding;
    SharedPrefs sharedPrefs;
    Context context;
    private static final String TAG = "AboutFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        sharedPrefs = new SharedPrefs(context);

        actionViews();

    }

    private void actionViews() {

        apiGetBookings();

    }

    private void apiGetBookings() {
        String url = "";

        if (sharedPrefs.getUser().type == Global.MECH_TYPE) {
            url = WebServices.API_GET_BOOKINGS_BYMECH + sharedPrefs.getUser().id;
        } else if (sharedPrefs.getUser().type == Global.CUSTOMER_TYPE) {
            url = WebServices.API_GET_BOOKINGS_BYCUSTOMER + sharedPrefs.getUser().id;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

            JSONArray jsonArray = response.optJSONArray("Booking");

            for (int i =0 ; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.optJSONObject(i);

                Booking booking= new Booking();

                booking.id = object.optString("_id");
                booking.service = object.optString("service");
                booking.userId = object.optString("userId");
                booking.mechanicId = object.optString("mechanicId");
                booking.lng = object.optString("longitude");
                booking.lat = object.optString("latitude");
                booking.time = object.optString("time");
                booking.lng = object.optString("longitude");
                booking.lng = object.optString("longitude");

            }

        }, error -> {

            Log.e(TAG, "apiGetBookings: ", error);
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
}