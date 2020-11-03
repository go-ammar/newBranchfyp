package com.electrosoft.electrosoftnew.ui;

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
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.adapters.RoomAdapter;
import com.electrosoft.electrosoftnew.adapters.SensorPerRoomAdapter;
import com.electrosoft.electrosoftnew.databinding.FragmentSensorPerRoomBinding;
import com.electrosoft.electrosoftnew.models.GetRoom;
import com.electrosoft.electrosoftnew.models.Sensor;
import com.electrosoft.electrosoftnew.models.Sensors;
import com.electrosoft.electrosoftnew.models.SensorsPerRoom;
import com.electrosoft.electrosoftnew.sharedprefs.SharedPrefs;
import com.electrosoft.electrosoftnew.webservices.VolleySingleton;
import com.electrosoft.electrosoftnew.webservices.WebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SensorPerRoomFragment extends Fragment {


    ArrayList<SensorsPerRoom> list;
    ArrayList<Sensors> sensorList = new ArrayList<>();

    NavController navController;
    private String URL;
    FragmentSensorPerRoomBinding binding;
    private RecyclerView recycle;
    private SensorPerRoomAdapter adapter;
    String TAG = "SensorPerRoomFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensor_per_room, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recycle = binding.sensorlist;
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(requireContext()));

        list = new ArrayList<>();

        actionViews();

    }

    private void actionViews() {

        SensorsPerRoom spr = new SensorsPerRoom();
        spr.description = "des";
        spr.name = "name";
        list.add(spr);
        SensorsPerRoom spr2 = new SensorsPerRoom();
        spr2.description = "des2";
        spr2.name = "name2";
        list.add(spr2);

//        adapter = new SensorPerRoomAdapter(list, getContext());
//        recycle.setAdapter(adapter);

        //ID for API
        SensorPerRoomFragmentArgs id = SensorPerRoomFragmentArgs.fromBundle(getArguments());


        SharedPrefs sharedPrefs = new SharedPrefs(getContext());
        JSONObject params = new JSONObject();

        //TODO remove this test id and add id.getId() in its place
//        String testId = "5f637ce0784173069b60c9c9";

        Log.d(TAG, "actionViews: get sensors api " + id.getId());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebServices.API_GET_SENSORS + id.getId(), params, response -> {

            Log.d(TAG, "_apigetSensors: res " + response);

            Sensors getSensors = new Sensors();

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create();
            getSensors = gson.fromJson(response.toString(), Sensors.class);
            sensorList.add(getSensors);
            Log.d(TAG, "actionViews: " + getSensors.toString());

            binding.progress.setVisibility(View.GONE);


            adapter = new SensorPerRoomAdapter(sensorList, getContext(), id.getId());
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