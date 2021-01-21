package com.electrosoft.electrosoftnew.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.electrosoft.electrosoftnew.Interfaces.ConfigurationInterface;
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.adapters.ConfigurationAdapter;
import com.electrosoft.electrosoftnew.databinding.ChangeRoomDialogBinding;
import com.electrosoft.electrosoftnew.databinding.FragmentDeviceConfigurationBinding;
import com.electrosoft.electrosoftnew.databinding.GetconfigurationdesignBinding;
import com.electrosoft.electrosoftnew.models.Devices;
import com.electrosoft.electrosoftnew.models.GetRoom;
import com.electrosoft.electrosoftnew.sharedprefs.SharedPrefs;
import com.electrosoft.electrosoftnew.webservices.VolleySingleton;
import com.electrosoft.electrosoftnew.webservices.WebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class DeviceConfigurationFragment extends Fragment implements ConfigurationInterface {

    FragmentDeviceConfigurationBinding binding;
    private static final String TAG = "DeviceConfigFragment";

    private ArrayList<Devices> devicesList = new ArrayList<>();

    NavController navController;
    private RecyclerView recycle;
    private ConfigurationAdapter adapter;
    private Context context;
    private ArrayList<GetRoom> getRoomList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = getContext();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_configuration, container, false);
        context = getContext();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        recycle = binding.configurationList;
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(context));

        binding.pullToRefresh.setOnRefreshListener(() -> {
            binding.progress.setVisibility(View.VISIBLE);
            devicesList.clear();
            actionViews(false);
            binding.pullToRefresh.setRefreshing(false);
        });
        actionViews(false);

    }

    private void actionViews(boolean check) {

        JSONObject params = new JSONObject();

        if (check){
            binding.progress.setVisibility(View.GONE);
        }
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        Log.d(TAG, "access token is: in Config" + sharedPrefs.getKey());
        try {

            // BODY
//            params.put("Authorization", sharedPrefs.getKey());
            Log.d(TAG, "actionViews: in config " + sharedPrefs.getKey());

            //        Log.d(TAG, "validations: email is:  " + binding.emailEt.getText().toString());
        } catch (Exception e) {
            Log.e(TAG, "_apiDevices: ", e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_DEVICES, params, response -> {

            devicesList.clear();
            Log.d(TAG, "_apigetDevices: res " + response);


            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create();
            Devices getDevices = gson.fromJson(response.toString(), Devices.class);

            Log.d(TAG, "actionViews: " + getDevices.toString());
            devicesList.add(getDevices);


            Log.d(TAG, "actionViews: get room is " + devicesList.get(0).toString());

            binding.progress.setVisibility(View.GONE);
            adapter = new ConfigurationAdapter(context, devicesList, this);
            recycle.setAdapter(adapter);

            Log.d(TAG, "actionViews: List is " + devicesList.toString());

        }, error -> {
            binding.progress.setVisibility(View.GONE);

            Toast toast = Toast.makeText(context, "Could not get devices", Toast.LENGTH_SHORT);
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

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }


    @Override
    public boolean itemChange(int position, String room_id, ArrayList<GetRoom> getRoomList, String room_name) {

        AtomicBoolean check = new AtomicBoolean(false);
        ChangeRoomDialogBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.change_room_dialog,
                null, false);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding2.getRoot())
                .create();
        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        GetconfigurationdesignBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.getconfigurationdesign,
                null, false);

        JSONObject params = new JSONObject();
        SharedPrefs sharedPrefs = new SharedPrefs(context);

        Log.d(TAG, "roomsString: " + binding1.viewrooms.getText().toString());
        if (!room_name.equals(""))
            dialog.show();


        //Doing this to get RoomID and send to params
        for (int i = 0; i < getRoomList.get(0).data.size(); i++) {
            Log.d(TAG, "itemChange: size " + room_name);
            if (room_name.
                    equals(getRoomList.get(0).data.get(i).name)) {
                Log.d(TAG, "itemChange: params is " + getRoomList.get(0).data.get(i).id);

                try {
                    Log.d(TAG, "itemChange: params is " + getRoomList.get(0).data.get(i).id);
                    params.put("roomId", getRoomList.get(0).data.get(i).id);
                    break;
                } catch (JSONException e) {
                    Log.d(TAG, "itemChange: error ");
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "itemChange: params is " + params);


        binding2.cancelButton.setOnClickListener(v -> {
//                    ((ConfigViewHolder) holder).binding.viewrooms.setText(finalRoom_id);
            dialog.dismiss();
            actionViews(false);
        });

        binding2.confirmButton.setOnClickListener(v -> {


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                            WebServices.API__UPDATE_CONFIG_DEVICE + devicesList.get(0).data.get(position).id + "/assignRoom",
                            params, response -> {

                        Toast toast = Toast.makeText(context, "Device location Updated", Toast.LENGTH_SHORT);
                        toast.show();

                        binding.progress.setVisibility(View.VISIBLE);
                        devicesList.clear();
                        actionViews(true);

                        check.set(true);


                    }, error -> {
                        Toast toast = Toast.makeText(context, "Device location not Updated", Toast.LENGTH_SHORT);
                        toast.show();
                        Log.d("TAG", "_apiGetRoom: error2 " + error);

                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap header = new HashMap<>();
                            header.put("Authorization", sharedPrefs.getKey());
                            Log.d("TAG", "getHeaders: " + header.toString());
                            return header;
                        }
                    };


                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
                    dialog.dismiss();

                }



        );

        return check.get();

    }

    @Override
    public void getDevices() {

    }
}


















