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
import com.fypapplication.fypapp.adapters.DevicesPerRoomAdapter;
import com.fypapplication.fypapp.databinding.FragmentDevicePerRoomBinding;
import com.fypapplication.fypapp.models.DeviceModelClass;
import com.fypapplication.fypapp.models.Devices;
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


public class DevicePerRoomFragment extends Fragment {


    ArrayList<Sensors> sensorList = new ArrayList<>();

    NavController navController;
    private String URL;
    FragmentDevicePerRoomBinding binding;
    private RecyclerView recycle;
    private DevicesPerRoomAdapter adapter;
    String TAG = "SensorPerRoomFragment";
    private Context mContext;

    private ArrayList<Devices> devicesList = new ArrayList<>();

    private ArrayList<DeviceModelClass> deviceModel = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_per_room, container, false);
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

        getDevices();

    }

    private void getDevices() {

        DevicePerRoomFragmentArgs id = DevicePerRoomFragmentArgs.fromBundle(getArguments());


        SharedPrefs sharedPrefs = new SharedPrefs(mContext);

        JSONObject params = new JSONObject();

        Log.d(TAG, "getDevices: ");

        deviceModel.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_DEVICES, params, response -> {

            Log.d(TAG, "_apigetDevices: res " + response);


            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create();
            Devices getDevices = gson.fromJson(response.toString(), Devices.class);

            Log.d(TAG, "actionViews: " + getDevices.toString());
            devicesList.add(getDevices);


            Log.d(TAG, "actionViews: get room is " + devicesList.get(0).data.size());

            binding.progress.setVisibility(View.GONE);


            for (int i = 0; devicesList.get(0).data.size() > i; i++) {

                if (devicesList.get(0).data.get(i).presentIn != null)
                    if (devicesList.get(0).data.get(i).presentIn._id.equals(id.getId())) {
                        DeviceModelClass dmc = new DeviceModelClass();
                        dmc.deviceName = devicesList.get(0).data.get(i).name;
                        dmc.id = devicesList.get(0).data.get(i).id;
                        deviceModel.add(dmc);
                    }
            }

            if (deviceModel.size() == 0) {
                binding.tv2.setVisibility(View.VISIBLE);
            }

            adapter = new DevicesPerRoomAdapter(deviceModel, mContext, id.getId());
            recycle.setAdapter(adapter);

            Log.d(TAG, "actionViews: List is " + devicesList.toString());

        }, error -> {
            binding.progress.setVisibility(View.GONE);

            Toast toast = Toast.makeText(mContext, "Could not get devices", Toast.LENGTH_SHORT);
            toast.show();
            Log.d(TAG, "_apiGetDevices: error " + error);

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap<>();
                header.put("Authorization", sharedPrefs.getKey());
                Log.d(TAG, "getHeaders: " + header.toString());
                return header;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

//    void _apiGetRooms() {
//
//
//        binding.progress.setVisibility(View.VISIBLE);
//        getRoomList.clear();
//        binding.pullToRefresh.setRefreshing(false);
//
//        JSONObject params = new JSONObject();
//
//        SharedPrefs sharedPrefs = new SharedPrefs(mContext);
//        Log.d(TAG, "access token is: in getroom" + sharedPrefs.getKey());
//        try {
//
//            // BODY
//            params.put("Authorization", sharedPrefs.getKey());
//            Log.d(TAG, "actionViews: in getroom " + sharedPrefs.getKey());
//
//            //        Log.d(TAG, "validations: email is:  " + binding.emailEt.getText().toString());
//        } catch (Exception e) {
//            Log.e(TAG, "_apiLogin: ", e);
//        }
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_ROOMS, params,
//                response -> {
//
//
//
//                    Log.d(TAG, "_apiGetRooms: " + response);
//
//                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
//                            .create();
//                    GetRoom getRoom1 = gson.fromJson(response.toString(), GetRoom.class);
//
//                    getRoomList.clear();
//
//                    getRoomList.add(getRoom1);
//
//                    binding.progress.setVisibility(View.GONE);
//                    roomAdapter = new RoomAdapter(mContext, getRoomList, this);
//                    Log.d(TAG, "actionViews: adapter called " + getRoomList.get(0).data.size());
//                    recycle.setAdapter(roomAdapter);
//
//
//                }, error -> {
//            binding.progress.setVisibility(View.GONE);
//            Toast toast = Toast.makeText(mContext, "Could not get rooms", Toast.LENGTH_SHORT);
//            toast.show();
//            Log.d(TAG, "_apiGetRoom: error " + error);
//
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap header = new HashMap<>();
//                header.put("Authorization", sharedPrefs.getKey());
//                Log.d(TAG, "getHeaders: " + header.toString());
//                return header;
//            }
//        };
//
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//
//    }

}