package com.fypapplication.fypapp;

import android.content.Context;
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
import com.fypapplication.fypapp.adapters.RemoveMechAdapter;
import com.fypapplication.fypapp.adapters.RoomAdapter;
import com.fypapplication.fypapp.databinding.FragmentRemoveMechsBinding;
import com.fypapplication.fypapp.models.GetRoom;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoveMechsFragment extends Fragment implements RemoveMechAdapter.RemoveMechInterface {

    private static final String TAG = "RemoveMechsFragment";
    FragmentRemoveMechsBinding binding;
    Context context;
    ArrayList<User> userArrayList;
    RemoveMechAdapter adapter;

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


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "test", params,
                response -> {

                    //TODO yahan se we'll get users, filter out users with type mech (a number) and then add to arraylist


                }, error -> {

            Log.e(TAG, "removeMech: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        adapter = new RemoveMechAdapter(context, userArrayList, this);

    }

    @Override
    public void removeMech(User user) {
        //API to Delete from database

        JSONObject params = new JSONObject();

        String id = user.id;

        SharedPrefs sharedPrefs = new SharedPrefs(context);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, WebServices.API_REMOVE_MECH + id, params,
                response -> {

                    Toast.makeText(context, "Mechanic Removed", Toast.LENGTH_SHORT).show();

                }, error -> {

            Log.e(TAG, "removeMech: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}