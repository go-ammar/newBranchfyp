package com.fypapplication.fypapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.RemoveMechAdapter;
import com.fypapplication.fypapp.databinding.DeleteRoomDialogBinding;
import com.fypapplication.fypapp.databinding.FragmentRemoveMechsBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RemoveMechsFragment extends Fragment implements RemoveMechAdapter.RemoveMechInterface {

    private static final String TAG = "RemoveMechsFragment";
    FragmentRemoveMechsBinding binding;
    Context context;
    ArrayList<User> userArrayList;
    RemoveMechAdapter adapter;
    ArrayList<String> address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_remove_mechs, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        userArrayList = new ArrayList<>();
        address = new ArrayList<>();
        actionViews();

    }

    private void actionViews() {

        //get mechs from API
        JSONObject params = new JSONObject();

        SharedPrefs sharedPrefs = new SharedPrefs(context);
        try {

//            params.put("Authorization", sharedPrefs.getKey());
            Log.d(TAG, "actionViews: in getroom " + sharedPrefs.getKey());

        } catch (Exception e) {
            Log.e(TAG, "removeMech: ", e);
        }


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_MECHS, null,
                response -> {


                    Log.d(TAG, "actionViews: " + response.length());
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

                            address.add(getAddress(Double.parseDouble(user.lat), Double.parseDouble(user.lng)));

                            userArrayList.add(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    adapter = new RemoveMechAdapter(context, userArrayList, this, address);
                    binding.serviceRecyclerView.setAdapter(adapter);

                }, error -> {

            Log.e(TAG, "removeMech: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    private String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);

            Log.d(TAG, "Address" + add);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return add;
    }

    @Override
    public void removeMech(User user) {
        //API to Delete from database

        DeleteRoomDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.delete_room_dialog,
                null, false);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding1.getRoot())
                .create();
        binding1.text.setText("Are you sure you want to remove the mechanic?");

        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding1.cancelButton.setText("No");
        binding1.cancelButton.setOnClickListener(view -> {

            dialog.dismiss();
        });

        binding1.confirmButton.setText("Yes");
        binding1.confirmButton.setOnClickListener(v -> {

            String id = user.id;

            Log.d(TAG, "removeMech: " + WebServices.API_REMOVE_MECH + id);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, WebServices.API_REMOVE_MECH + id, null,
                    response -> {

                        Toast.makeText(context, "Mechanic Removed", Toast.LENGTH_SHORT).show();

                    }, error -> {

                Log.e(TAG, "removeMech: ", error);

            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

        });

    }
}