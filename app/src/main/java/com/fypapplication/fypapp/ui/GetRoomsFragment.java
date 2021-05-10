package com.fypapplication.fypapp.ui;


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
import com.fypapplication.fypapp.Interfaces.RoomInterface;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.RoomAdapter;
import com.fypapplication.fypapp.databinding.DeleteRoomDialogBinding;
import com.fypapplication.fypapp.databinding.FragmentGetRoomsBinding;
import com.fypapplication.fypapp.databinding.UpdateRoomDialogBinding;
import com.fypapplication.fypapp.models.GetRoom;
import com.fypapplication.fypapp.models.Room;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetRoomsFragment extends Fragment implements RoomInterface {


    private static final String TAG = "GetRoomsFragment";
    private final ArrayList<GetRoom> getRoomList = new ArrayList<>();
    NavController navController;
    FragmentGetRoomsBinding binding;
    Context mContext = getContext();
    private String URL;
    private List<Room> lst;
    private RecyclerView recycle;
    private RoomAdapter roomAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_rooms, container, false);
        mContext = getContext();
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);
        recycle = binding.programmingList;
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(mContext));


        lst = new ArrayList<>();


//        GetRoomsFragmentArgs args = GetRoomsFragmentArgs.fromBundle(getArguments());


        binding.pullToRefresh.setOnRefreshListener(() -> {
            binding.progress.setVisibility(View.VISIBLE);
            getRoomList.clear();
            actionViews();
            binding.pullToRefresh.setRefreshing(false);
        });

        actionViews();
    }


    private void actionViews() {


        binding.addRoombtn.setOnClickListener(view -> {
            addRoomsDialog();
        });

        _apiGetRooms();


    }


    void _apiGetRooms() {


        binding.progress.setVisibility(View.VISIBLE);
//        getRoomList.clear();
//        binding.pullToRefresh.setRefreshing(false);

        JSONObject params = new JSONObject();

        SharedPrefs sharedPrefs = new SharedPrefs(mContext);
        Log.d(TAG, "access token is: in getroom" + sharedPrefs.getKey());
        try {

            // BODY
//            params.put("Authorization", sharedPrefs.getKey());
            Log.d(TAG, "actionViews: in getroom " + sharedPrefs.getKey());

            //        Log.d(TAG, "validations: email is:  " + binding.emailEt.getText().toString());
        } catch (Exception e) {
            Log.e(TAG, "_apiLogin: ", e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_ROOMS, params,
                response -> {


                    Log.d(TAG, "_apiGetRooms: " + response);

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                            .create();
                    GetRoom getRoom1 = gson.fromJson(response.toString(), GetRoom.class);

                    getRoomList.clear();

                    getRoomList.add(getRoom1);


                    binding.progress.setVisibility(View.GONE);
                    roomAdapter = new RoomAdapter(mContext, getRoomList, this);
                    Log.d(TAG, "actionViews: adapter called " + getRoomList.get(0).data.size());
                    recycle.setAdapter(roomAdapter);


                }, error -> {
            binding.progress.setVisibility(View.GONE);
            Toast toast = Toast.makeText(mContext, "Could not get rooms", Toast.LENGTH_SHORT);
            toast.show();
            Log.d(TAG, "_apiGetRoom: error " + error);

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

        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    void addRoomsDialog() {
//
//        AddRoomDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.add_change_dialog,
//                null, false);
//
//        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
//                .setView(binding.getRoot())
//                .create();
//
//        binding.addRoomBtn.setOnClickListener(view -> {
//            if (binding.romno.getEditText().getText().toString().length() == 0) {
//                alertDialog.dismiss();
//            }
//
//
//            JSONObject params = new JSONObject();
//
//            try {
//
//                // BODY
//                params.put("name", binding.romno.getEditText().getText().toString().trim());
//
//            } catch (Exception e) {
//                Log.e(TAG, "_apiLogin: ", e);
//            }
//
//            SharedPrefs sharedPrefs = new SharedPrefs(mContext);
//            Log.d(TAG, "access token is: in getroom" + sharedPrefs.getKey());
//            try {
//
//                Log.d(TAG, "actionViews: in getroom " + sharedPrefs.getKey());
//
//            } catch (Exception e) {
//                Log.e(TAG, "_apiLogin: ", e);
//            }
//
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_ADD_ROOM, params, response -> {
//
//                Log.d(TAG, "_apigetRoom: res " + response);
//                Toast toast = Toast.makeText(mContext, "Room added", Toast.LENGTH_SHORT);
//
//
//                toast.show();
//                alertDialog.dismiss();
//                actionViews();
//
//
//            }, error -> {
//                Toast toast = Toast.makeText(mContext, "Could not add room", Toast.LENGTH_SHORT);
//                toast.show();
//                Log.d(TAG, "_apiGetRoom: error " + error);
//                alertDialog.dismiss();
//
//
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap header = new HashMap<>();
//                    header.put("Authorization", sharedPrefs.getKey());
//                    Log.d(TAG, "getHeaders: " + header.toString());
//                    return header;
//                }
//            };
//
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//            VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//
//
//        });
//
//        binding.CancelBtn.setOnClickListener(view -> {
//            alertDialog.dismiss();
//        });
//
//        if (alertDialog.getWindow() != null)
//            alertDialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        alertDialog.show();
//
//
    }


    @Override
    public void updateDialog(int position) {


        UpdateRoomDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.update_room_dialog,
                null, false);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(binding.getRoot())
                .create();


        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.RoomIdET.setText(getRoomList.get(0).data.get(position).name);
        dialog.show();

        binding.CancelBtn.setOnClickListener(view -> {
            dialog.dismiss();
        });

        binding.updateRoomBtn.setOnClickListener(view -> {
            dialog.dismiss();

            JSONObject params = new JSONObject();

            try {
                // BODY
                params.put("name", binding.RoomIdET.getText().toString());
            } catch (Exception e) {
                Log.e("TAG", "_apiLogin: ", e);
            }


            SharedPrefs sharedPrefs = new SharedPrefs(mContext);


            //TODO checking values for updates


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                    WebServices.API_UPDATE_ROOM + getRoomList.get(0).data.get(position).id,
                    params, response -> {

                getRoomList.get(0).data.get(position).name = binding.RoomIdET.getText().toString();
                roomAdapter.notifyItemChanged(position);

                Log.d("TAG", "_apiUpdateRoom: res " + response);
                Toast toast = Toast.makeText(mContext, "Room updated", Toast.LENGTH_SHORT);
                toast.show();


            }, error -> {
                Toast toast = Toast.makeText(mContext, "Could not update room", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("TAG", "_apiUpdateRoom: error " + error);

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

            VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);


        });

    }

    @Override
    public void deleteDialog(int position) {

        DeleteRoomDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.delete_room_dialog,
                null, false);

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(binding.getRoot())
                .create();
        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding.cancelButton.setOnClickListener(view -> {
            dialog.dismiss();
        });

        binding.confirmButton.setOnClickListener(view -> {


            JSONObject params = new JSONObject();
            SharedPrefs sharedPrefs = new SharedPrefs(mContext);


            Log.d("TAG", "showDeleteDialog: " + getRoomList.get(0).data.get(position).id);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                    WebServices.API_DELETE_ROOM + getRoomList.get(0).data.get(position).id,
                    params, response -> {

                Log.d("TAG", "_apigetRoom: res " + response);


                Toast toast = Toast.makeText(mContext, "Room deleted successfully", Toast.LENGTH_SHORT);
                toast.show();


            }, error -> {
                Toast toast = Toast.makeText(mContext, "Room could not be deleted", Toast.LENGTH_SHORT);
                toast.show();
                dialog.dismiss();


            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap header = new HashMap<>();
                    header.put("Authorization", sharedPrefs.getKey());
                    Log.d("TAG", "getHeaders: " + header.toString());
                    return header;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

        });
    }
}
