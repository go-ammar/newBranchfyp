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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.MechServicesAdapter;
import com.fypapplication.fypapp.databinding.AddChangeDialogBinding;
import com.fypapplication.fypapp.databinding.AddServiceDialogBinding;
import com.fypapplication.fypapp.databinding.FragmentMyServicesBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.ChangesDue;
import com.fypapplication.fypapp.models.MechServices;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;

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

                        mechServicesArrayList.add(mechServices);

                        sharedPrefs.putMechServicesList(mechServicesArrayList);

                        adapter.notifyDataSetChanged();

                        //TODO CALL API TO CHANGE SERVICE IN BACKEND

                        alertDialog.dismiss();

                    }
            });

            alertDialog.show();
        });

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


        //TODO API WHERE DELETE STUFF HAPPENS IN DB

        adapter.notifyDataSetChanged();
    }
}