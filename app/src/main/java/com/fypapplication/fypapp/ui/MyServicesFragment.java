package com.fypapplication.fypapp.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.MechServicesAdapter;
import com.fypapplication.fypapp.databinding.AddChangeDialogBinding;
import com.fypapplication.fypapp.databinding.AddServiceDialogBinding;
import com.fypapplication.fypapp.databinding.FragmentMyServicesBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.ChangesDue;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.models.MechServices;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

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

        mechServicesArrayList = sharedPrefs.getMechServices();
        adapter = new MechServicesAdapter(mContext, mechServicesArrayList, this);
        binding.serviceRecyclerView.setAdapter(adapter);


        binding.addServiceBtn.setOnClickListener(view -> {
            AddServiceDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.add_service_dialog,
                    null, false);

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

            sharedPrefs.putMechServicesList(mechServicesArrayList);

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

        //TODO API WHERE DELETE STUFF HAPPENS IN DB

        adapter.notifyDataSetChanged();
    }
}