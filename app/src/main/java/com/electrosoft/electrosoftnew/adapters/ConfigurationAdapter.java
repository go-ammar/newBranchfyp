package com.electrosoft.electrosoftnew.adapters;

import android.app.AlertDialog;
import android.content.Context;

import static java.util.Objects.isNull;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.electrosoft.electrosoftnew.Interfaces.ConfigurationInterface;
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.DeleteRoomDialogBinding;
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
import java.util.List;
import java.util.Map;

public class ConfigurationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<Devices> mdata;
    private ArrayList<GetRoom> getRoomList = new ArrayList<>();
    GetRoom getRoom1;
    private ConfigurationInterface configurationInterface;


    public ConfigurationAdapter(Context context, List<Devices> mdata, ConfigurationInterface cf) {
        this.context = context;
        this.mdata = mdata;
        configurationInterface = cf;
    }


    @NonNull
    @Override
    public ConfigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GetconfigurationdesignBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.getconfigurationdesign, parent,
                false);
        return new ConfigViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Devices config = mdata.get(0);
        ((ConfigViewHolder) holder).setBinding(config);
        String room_id = "";

        DeleteRoomDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.delete_room_dialog,
                null, false);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .create();
        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (mdata.get(0).data.size() > 0) {


            ((ConfigViewHolder) holder).binding.descriptionTextview.setText(mdata.get(0).data.get(position).description);

            ((ConfigViewHolder) holder).binding.roomNameTextview.setText(mdata.get(0).data.get(position).name);

            if (!isNull(mdata.get(0).data.get(position).presentIn))
                ((ConfigViewHolder) holder).binding.viewrooms.setText(mdata.get(0).data.get(position).presentIn.name);

            room_id = ((ConfigViewHolder) holder).binding.viewrooms.getText().toString();
        }
        //TODO get rooms for drop down

        JSONObject params = new JSONObject();
        SharedPrefs sharedPrefs = new SharedPrefs(context);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_ROOMS,
                params, response -> {

            Log.d("TAG", "_apigetRoom: res " + response);

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create();
            getRoom1 = gson.fromJson(response.toString(), GetRoom.class);
            getRoomList.add(getRoom1);

            String[] data = new String[getRoomList.get(0).data.size()];


            for (int i = 0; i < data.length; i++) {
                data[i] = getRoomList.get(0).data.get(i).name;
            }

            ArrayAdapter<String> dropDown = new ArrayAdapter<>(
                    context, android.R.layout.simple_list_item_1, data
            );

            ((ConfigViewHolder) holder).binding.viewrooms.setInputType(0);

            ((ConfigViewHolder) holder).binding.viewrooms.setOnClickListener(v -> {

                ((ConfigViewHolder) holder).binding.viewrooms.setAdapter(dropDown);
                ((ConfigViewHolder) holder).binding.viewrooms.showDropDown();
                ((ConfigViewHolder) holder).binding.viewrooms.requestFocus();


            });


        }, error -> {
            Toast toast = Toast.makeText(context, "Could not get rooms", Toast.LENGTH_SHORT);
            toast.show();
            Log.d("TAG", "_apiGetRoom: error " + error);

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


        String finalRoom_id = room_id;


        ((ConfigViewHolder) holder).binding.viewrooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                configurationInterface.itemChange(position, finalRoom_id, getRoomList,
                        ((ConfigViewHolder) holder).binding.viewrooms.getText().toString());
            }
        });

//        ((ConfigViewHolder) holder).binding.viewrooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
//
//                if (!finalRoom_id.equals(((ConfigViewHolder) holder).binding.viewrooms.getText().toString()))
//                    dialog.show();
//
//
//                //Doing this to get RoomID and send to params
//                for (int i = 0; i < getRoomList.get(0).data.size(); i++) {
//                    if (((ConfigViewHolder) holder).binding.viewrooms.getText().toString().
//                            equals(getRoomList.get(0).data.get(i).name)) {
//                        try {
//                            params.put("roomId", getRoomList.get(0).data.get(i).id);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//
//                binding.cancelButton.setOnClickListener(v -> {
////                    ((ConfigViewHolder) holder).binding.viewrooms.setText(finalRoom_id);
//                    dialog.dismiss();
//                });
//
//                binding.confirmButton.setOnClickListener(v -> {
//
//                    SharedPrefs sharedPrefs = new SharedPrefs(context);
//
//                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
//                                    WebServices.API__UPDATE_CONFIG_DEVICE + mdata.get(0).data.get(position).id + "/assignRoom",
//                                    params, response -> {
//
//                                Toast toast = Toast.makeText(context, "Device location Updated", Toast.LENGTH_SHORT);
//                                toast.show();
//
//
//                            }, error -> {
//                                Toast toast = Toast.makeText(context, "Device location not Updated", Toast.LENGTH_SHORT);
//                                toast.show();
//                                Log.d("TAG", "_apiGetRoom: error2 " + error);
//
//                            }) {
//                                @Override
//                                public Map<String, String> getHeaders() throws AuthFailureError {
//                                    HashMap header = new HashMap<>();
//                                    header.put("Authorization", sharedPrefs.getKey());
//                                    Log.d("TAG", "getHeaders: " + header.toString());
//                                    return header;
//                                }
//                            };
//
//
//                            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
//                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
//                            dialog.dismiss();
//
//                        }
//
//                );
//
//
//            }
//        });

//        ((ConfigViewHolder) holder).binding.viewrooms.addTextChangedListener(new TextWatcher() {
//
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
////                dialog.show();
//
////                JSONObject params = new JSONObject();
////
////
////                for (int i = 0; i < getRoomList.get(0).data.size(); i++) {
////
////                    if (((ConfigViewHolder) holder).binding.viewrooms.getText().toString().
////                            equals(getRoomList.get(0).data.get(i).name)) {
////                        try {
////                            params.put("roomId", getRoomList.get(0).data.get(i).id);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
////
////
////                SharedPrefs sharedPrefs = new SharedPrefs(context);
////
////
////                binding.cancelButton.setOnClickListener(view -> {
////                    ((ConfigViewHolder) holder).binding.viewrooms.setText(finalRoom_id);
////                    dialog.dismiss();
////                });
////
////                binding.confirmButton.setOnClickListener(view -> {
////
////                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
////                                    WebServices.API__UPDATE_CONFIG_DEVICE + mdata.get(0).data.get(position).id + "/assignRoom",
////                                    params, response -> {
////
////
////                                Toast toast = Toast.makeText(context, "Device location Updated", Toast.LENGTH_SHORT);
////                                toast.show();
////
////
////                            }, error -> {
////                                Toast toast = Toast.makeText(context, "Device location not Updated", Toast.LENGTH_SHORT);
////                                toast.show();
////                                Log.d("TAG", "_apiGetRoom: error2 " + error);
////
////                            }) {
////                                @Override
////                                public Map<String, String> getHeaders() throws AuthFailureError {
////                                    HashMap header = new HashMap<>();
////                                    header.put("Authorization", sharedPrefs.getKey());
////                                    Log.d("TAG", "getHeaders: " + header.toString());
////                                    return header;
////                                }
////                            };
////
////
////                            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
////                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
////                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
////
////                            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
////                            dialog.dismiss();
////
////                        }
////
////                );
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount in config is: " + mdata.size());
        if (!isNull(mdata.get(0)))
            return mdata.get(0).data.size();
        else return 0;
    }

    public static class ConfigViewHolder extends RecyclerView.ViewHolder {

        GetconfigurationdesignBinding binding;


        public ConfigViewHolder(@NonNull GetconfigurationdesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void setBinding(Devices configuration) {
            binding.setConfig(configuration);
            binding.executePendingBindings();
        }

    }
}
