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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.RemoveMechAdapter;
import com.fypapplication.fypapp.databinding.FragmentRemoveMechsBinding;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_USERS + "/mechs", null,
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

                            userArrayList.add(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    adapter = new RemoveMechAdapter(context, userArrayList, this);
                    binding.serviceRecyclerView.setAdapter(adapter);

                }, error -> {

            Log.e(TAG, "removeMech: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }

    @Override
    public void removeMech(User user) {
        //API to Delete from database

        JSONObject params = new JSONObject();

        String id = user.id;

        SharedPrefs sharedPrefs = new SharedPrefs(context);


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
    }
}