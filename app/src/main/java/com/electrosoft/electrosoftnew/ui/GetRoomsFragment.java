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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.electrosoft.electrosoftnew.Interfaces.RoomInterface;
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.adapters.RoomAdapter;
import com.electrosoft.electrosoftnew.databinding.DeleteRoomDialogBinding;
import com.electrosoft.electrosoftnew.databinding.FragmentGetRoomsBinding;
import com.electrosoft.electrosoftnew.databinding.AddRoomDialogBinding;
import com.electrosoft.electrosoftnew.databinding.UpdateRoomDialogBinding;
import com.electrosoft.electrosoftnew.models.GetRoom;
import com.electrosoft.electrosoftnew.models.Room;
import com.electrosoft.electrosoftnew.sharedprefs.SharedPrefs;
import com.electrosoft.electrosoftnew.webservices.VolleySingleton;
import com.electrosoft.electrosoftnew.webservices.WebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class GetRoomsFragment extends Fragment implements RoomInterface {


    private static final String TAG = "GetRoomsFragment";
    private String URL;
    private List<Room> lst;
    NavController navController;
    private RecyclerView recycle;
    private RoomAdapter roomAdapter;
    FragmentGetRoomsBinding binding;
    private ArrayList<GetRoom> getRoomList = new ArrayList<>();
    Context mContext = getContext();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_rooms, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);
        recycle = binding.programmingList;
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(requireContext()));

        lst = new ArrayList<>();


//        GetRoomsFragmentArgs args = GetRoomsFragmentArgs.fromBundle(getArguments());


        binding.pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.progress.setVisibility(View.VISIBLE);
                getRoomList.clear();
                actionViews();
                binding.pullToRefresh.setRefreshing(false);
            }
        });

        actionViews();
    }


    private void actionViews() {


        binding.addRoombtn.setOnClickListener(view -> {
            addRoomsDialog();
        });

        _apiGetRooms();

        //        JSONObject params = new JSONObject();
//
//        SharedPrefs sharedPrefs = new SharedPrefs(getContext());
//        Log.d(TAG, "access token is: in getroom" + sharedPrefs.getKey());
//        try {
//
//            // BODY
////            params.put("Authorization", sharedPrefs.getKey());
//            Log.d(TAG, "actionViews: in getroom " + sharedPrefs.getKey());
//
//            //        Log.d(TAG, "validations: email is:  " + binding.emailEt.getText().toString());
//        } catch (Exception e) {
//            Log.e(TAG, "_apiLogin: ", e);
//        }
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_ROOMS, params, response -> {
//
//
////            JsonAdapter<GetRoom> jsonAdapter = moshi.adapter(GetRoom.class);
//
//
//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
//                    .create();
//            GetRoom getRoom1 = gson.fromJson(response.toString(), GetRoom.class);
//            getRoomList.add(getRoom1);
//
//
//            binding.progress.setVisibility(View.GONE);
//            roomAdapter = new RoomAdapter(requireContext(), getRoomList, this);
//            Log.d(TAG, "actionViews: adapter called ");
//            recycle.setAdapter(roomAdapter);
//
//
//        }, error -> {
//            binding.progress.setVisibility(View.GONE);
//            Toast toast = Toast.makeText(getContext(), "Could not get rooms", Toast.LENGTH_SHORT);
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
//        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }


    void _apiGetRooms(){


        binding.progress.setVisibility(View.VISIBLE);
//        getRoomList.clear();
//        binding.pullToRefresh.setRefreshing(false);

        JSONObject params = new JSONObject();

        SharedPrefs sharedPrefs = new SharedPrefs(getContext());
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


//            JsonAdapter<GetRoom> jsonAdapter = moshi.adapter(GetRoom.class);


            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create();
            GetRoom getRoom1 = gson.fromJson(response.toString(), GetRoom.class);

                    getRoomList.clear();

                    getRoomList.add(getRoom1);


            binding.progress.setVisibility(View.GONE);
            roomAdapter = new RoomAdapter(requireContext(), getRoomList, this);
            Log.d(TAG, "actionViews: adapter called "+ getRoomList.get(0).data.size());
            recycle.setAdapter(roomAdapter);


        }, error -> {
            binding.progress.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getContext(), "Could not get rooms", Toast.LENGTH_SHORT);
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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

    void addRoomsDialog() {

        AddRoomDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.add_room_dialog,
                null, false);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(binding.getRoot())
                .create();

        binding.addRoomBtn.setOnClickListener(view -> {
            if (binding.romno.getEditText().getText().toString().length() == 0) {
                alertDialog.dismiss();
            }


            JSONObject params = new JSONObject();

            try {

                // BODY
                params.put("name", binding.romno.getEditText().getText().toString().trim());

            } catch (Exception e) {
                Log.e(TAG, "_apiLogin: ", e);
            }

            SharedPrefs sharedPrefs = new SharedPrefs(getContext());
            Log.d(TAG, "access token is: in getroom" + sharedPrefs.getKey());
            try {

                Log.d(TAG, "actionViews: in getroom " + sharedPrefs.getKey());

            } catch (Exception e) {
                Log.e(TAG, "_apiLogin: ", e);
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_ADD_ROOM, params, response -> {

                Log.d(TAG, "_apigetRoom: res " + response);
                Toast toast = Toast.makeText(getContext(), "Room added", Toast.LENGTH_SHORT);


                toast.show();
                alertDialog.dismiss();
                actionViews();



            }, error -> {
                Toast toast = Toast.makeText(getContext(), "Could not add room", Toast.LENGTH_SHORT);
                toast.show();
                Log.d(TAG, "_apiGetRoom: error " + error);
                alertDialog.dismiss();


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


        });

        binding.CancelBtn.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.show();


    }


    @Override
    public void updateDialog(int position) {

        mContext = getContext();

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

        mContext = getContext();
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
//            dialog.dismiss();


            JSONObject params = new JSONObject();
            SharedPrefs sharedPrefs = new SharedPrefs(mContext);


            Log.d("TAG", "showDeleteDialog: " + getRoomList.get(0).data.get(position).id);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                    WebServices.API_DELETE_ROOM + getRoomList.get(0).data.get(position).id,
                    params, response -> {

                Log.d("TAG", "_apigetRoom: res " + response);


                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                        .create();
                Toast toast = Toast.makeText(mContext, "Room deleted successfully", Toast.LENGTH_SHORT);
                toast.show();


            }, error -> {
                Toast toast = Toast.makeText(mContext, "Room could not be deleted", Toast.LENGTH_SHORT);
                toast.show();
                dialog.dismiss();


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
}
