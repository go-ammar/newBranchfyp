package com.fypapplication.fypapp.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.MechServicesAdapter;
import com.fypapplication.fypapp.databinding.AddChangeDialogBinding;
import com.fypapplication.fypapp.databinding.AddServiceDialogBinding;
import com.fypapplication.fypapp.databinding.DeleteRoomDialogBinding;
import com.fypapplication.fypapp.databinding.FragmentMyServicesBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.ChangesDue;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.models.MechServices;
import com.fypapplication.fypapp.models.Services;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MyServicesFragment extends Fragment implements MechServicesAdapter.MyServicesInterface {

    private static final String TAG = "MyServicesFragment";
    FragmentMyServicesBinding binding;
    Context mContext;
    ArrayList<MechServices> mechServicesArrayList;
    SharedPrefs sharedPrefs;
    MechServicesAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_services, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        mechServicesArrayList = new ArrayList<>();
        sharedPrefs = new SharedPrefs(mContext);
        actionViews();
    }

    private void actionViews() {

        apiGetServices();

        binding.addServiceBtn.setOnClickListener(view -> {
            AddServiceDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.add_service_dialog,
                    null, false);


            String[] data = {"Car", "Bike", "Rickshaw", "Truck"};

            ArrayAdapter<String> dropDown = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
            binding1.vehicleTypeET.setInputType(0);

            binding1.vehicleTypeET.setOnClickListener(v -> {
                binding1.vehicleTypeET.setAdapter(dropDown);
                binding1.vehicleTypeET.showDropDown();
                binding1.vehicleTypeET.requestFocus();
            });

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setView(binding1.getRoot())
                    .create();


            binding1.CancelBtn.setOnClickListener(v -> {
                alertDialog.dismiss();
            });


            binding1.addServiceBtn.setOnClickListener(v -> {

                if (Objects.requireNonNull(binding1.vehicleTypeET.getText()).toString().equalsIgnoreCase(Global.VEHICLE_BIKE)
                        || binding1.vehicleTypeET.getText().toString().equalsIgnoreCase(Global.VEHICLE_CAR)
                        || binding1.vehicleTypeET.getText().toString().equalsIgnoreCase(Global.VEHICLE_RICKSHAW)
                        || binding1.vehicleTypeET.getText().toString().equalsIgnoreCase(Global.VEHICLE_TRUCK))
                    if (!Objects.requireNonNull(binding1.priceET.getText()).toString().isEmpty() && !Objects.requireNonNull(binding1.serviceNameET.getText()).toString().isEmpty()) {

                        mechServicesArrayList = sharedPrefs.getMechServices();

                        MechServices mechServices = new MechServices();
                        mechServices.price = Integer.parseInt(binding1.priceET.getText().toString());
                        mechServices.service = binding1.serviceNameET.getText().toString();
                        mechServices.vehicleType = binding1.vehicleTypeET.getText().toString();


                        //TODO CALL API TO CHANGE SERVICE IN BACKEND
                        apiAddService(mechServices.service, mechServices.price, mechServices.vehicleType);

                        alertDialog.dismiss();

                    }
            });

            alertDialog.show();
        });

    }

    private void apiGetServices() {

        String id = sharedPrefs.getUser().id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_GET_SERVICE_BYMECH + id, null, res -> {


            try {
                JSONArray service = res.getJSONArray("Service");

                for (int i = 0; i < service.length(); i++) {
                    MechServices mechServices = new MechServices();
                    JSONObject obj = service.getJSONObject(i);
                    mechServices.price = obj.optInt("price");
                    mechServices.service = obj.optString("service_name");
                    mechServices.vehicleType = obj.optString("vehicle_type");
                    mechServices.id = obj.optString("_id");
                    mechServicesArrayList.add(mechServices);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new MechServicesAdapter(mContext, mechServicesArrayList, this, false);
            binding.serviceRecyclerView.setAdapter(adapter);


        }, error -> {

            Log.e(TAG, "loginApi: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }

    private void apiAddService(String service_name, int price, String vehicle_type) {
        JSONObject params = new JSONObject();

        try {
            // BODY
            params.put("service_name", service_name);
            params.put("price", price);
            params.put("vehicle_type", vehicle_type);
            params.put("mechanic", sharedPrefs.getUser().id);
        } catch (Exception e) {
            Log.e(TAG, "_apiLogin: ", e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_DELETE_SERVICES, params, res -> {

            Toast.makeText(mContext, "Service Added", Toast.LENGTH_SHORT).show();
            MechServices mechServices = new MechServices();
            mechServices.price = price;
            mechServices.service = service_name;
            mechServices.vehicleType = vehicle_type;
            mechServices.id = res.optString("id");

            mechServicesArrayList.add(mechServices);

//            sharedPrefs.putMechServicesList(mechServicesArrayList);

            adapter.notifyDataSetChanged();


        }, error -> {

            Log.e(TAG, "loginApi: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }


    @Override
    public void deleteService(MechServices mechServices) {


        DeleteRoomDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.delete_room_dialog,
                null, false);
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(binding1.getRoot())
                .create();
        binding1.text.setText("Are you sure you want to remove the Service?");

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
            for (int i = 0; i < mechServicesArrayList.size(); i++) {
                MechServices cd = mechServicesArrayList.get(i);

                if (cd.vehicleType.equals(mechServices.vehicleType) && cd.service.equals(mechServices.service)) {
                    mechServicesArrayList.remove(i);
                }
            }

            sharedPrefs.putMechServicesList(mechServicesArrayList);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, WebServices.API_DELETE_SERVICES + mechServices.id, null, res -> {

                Toast.makeText(mContext, "Service Added", Toast.LENGTH_SHORT).show();

            }, error -> {

                Log.e(TAG, "loginApi: ", error);

            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

            adapter.notifyDataSetChanged();
        });

    }
}