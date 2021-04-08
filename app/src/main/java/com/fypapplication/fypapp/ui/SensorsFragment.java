package com.fypapplication.fypapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fypapplication.fypapp.adapters.SensorsPerDeviceAdapter;
import com.fypapplication.fypapp.databinding.FragmentSensorsBinding;
import com.fypapplication.fypapp.models.Sensors;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SensorsFragment extends Fragment {

    private Context mContext;
    FragmentSensorsBinding binding;
    private static final String TAG = "SensorsFragment";
    NavController navController;
    private RecyclerView recycle;
    ArrayList<Sensors> sensorList = new ArrayList<>();
    SensorsPerDeviceAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensors, container, false);
        mContext = getContext();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recycle = binding.sensorlist;
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(requireContext()));


        actionViews();

    }

    private void actionViews() {

        //TODO get deviceId from prev screen
        String deviceid = "5fc61460e4540cc13901fa7e";

        SensorsFragmentArgs sensorsFragmentArgs = SensorsFragmentArgs.fromBundle(getArguments());
        String id = sensorsFragmentArgs.getDeviceId();

        SharedPrefs sharedPrefs = new SharedPrefs(getContext());
        JSONObject params = new JSONObject();

        sensorList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebServices.API_GET_SENSORS + id, params, response -> {

            Log.d(TAG, "_apigetSensors: res " + response);

            Sensors getSensors = new Sensors();

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create();
            getSensors = gson.fromJson(response.toString(), Sensors.class);
            sensorList.add(getSensors);
            Log.d(TAG, "actionViews: " + getSensors.toString());

            binding.progress.setVisibility(View.GONE);

            if (sensorList.size() == 0){
                binding.tv2.setVisibility(View.VISIBLE);
            }

            adapter = new SensorsPerDeviceAdapter(sensorList, getContext());
            recycle.setAdapter(adapter);

        }, error -> {
            binding.progress.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getContext(), "Could not get Sensors", Toast.LENGTH_SHORT);
            toast.show();
            Log.d(TAG, "_apiGetSensors: error " + error);

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap<>();
                header.put("Authorization", sharedPrefs.getKey());
                Log.d(TAG, "getHeaders: " + header.toString());
                return header;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);




    }

}