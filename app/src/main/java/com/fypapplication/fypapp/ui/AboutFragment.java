package com.fypapplication.fypapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.BookingAdapter;
import com.fypapplication.fypapp.databinding.FragmentAboutBinding;
import com.fypapplication.fypapp.databinding.LayoutYouHaveABookingBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.Booking;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AboutFragment extends Fragment implements BookingAdapter.BookingInterface {

    private static final String TAG = "AboutFragment";
    FragmentAboutBinding binding;
    SharedPrefs sharedPrefs;
    Context context;
    private BookingAdapter adapter;

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

            ArrayList<Booking> bookingArrayList = new ArrayList<>();

            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject object = jsonArray.optJSONObject(i);

                Booking booking = new Booking();

                booking.service = "";

                if (object.optString("service").equalsIgnoreCase("emergency")) {
                    booking.service = object.optString("service");
                } else {
                    JSONArray jsonArr = null;
                    try {
                        jsonArr = new JSONArray(object.optString("service"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (jsonArr.length() > 0)
                        for (int j = 0; j < jsonArr.length(); j++) {
                            JSONObject jsonObj = jsonArr.optJSONObject(j);

                            if (j != jsonArr.length() - 1) {
                                Log.d(TAG, "Bookings: in loops " + jsonObj.optString("service"));
                                booking.service = booking.service + jsonObj.optString("service") + ", ";
                            } else if (j == jsonArr.length() - 1) {
                                booking.service = booking.service + jsonObj.optString("service");
                            }
                        }
                }

                booking.id = object.optString("_id");
                booking.userId = object.optString("userId");
                booking.mechanicId = object.optString("mechanicId");
                booking.lng = object.optString("longitude");
                booking.lat = object.optString("latitude");


                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
                try {
                    date = dateFormat.parse(object.optString("time"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat formatter = new SimpleDateFormat("MMM dd yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);
                booking.time = dateStr;
                booking.lng = object.optString("longitude");
                booking.lng = object.optString("longitude");

                bookingArrayList.add(booking);
            }
            adapter = new BookingAdapter(bookingArrayList, context, this);
            binding.recyler.setAdapter(adapter);
            binding.recyler.addItemDecoration(new DividerItemDecoration(binding.recyler.getContext(), DividerItemDecoration.VERTICAL));

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

    @Override
    public void onBookingClicked(Booking booking) {

        SharedPrefs sharedPrefs = new SharedPrefs(context);
        String id = "";
        if (sharedPrefs.getUser().type == Global.CUSTOMER_TYPE) {
            id = booking.mechanicId;
        } else if (sharedPrefs.getUser().type == Global.MECH_TYPE) {
            id = booking.userId;
        }

        LayoutYouHaveABookingBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_you_have_a_booking,
                null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding1.getRoot())
                .create();

        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_USERS + "/" + id, null, response -> {

            User user = new User();
            Log.d(TAG, "openDialogForNewBooking: " + response);

            user.phoneNumber = response.optString("phone");
            user.id = response.optString("_id");
            user.name = response.optString("name");
            user.type = response.optString("type");
            user.email = response.optString("email");
            user.lat = response.optString("latitude");
            user.lng = response.optString("longitude");

            binding1.setMechanic(user);

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
}