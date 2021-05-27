package com.fypapplication.fypapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.ServicesAdapter;
import com.fypapplication.fypapp.databinding.FragmentServicesBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.Services;

import java.util.ArrayList;

import cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException;

public class ServicesFragment extends Fragment {

    private static final String TAG = "ServicesFragment";
    private final ArrayList<Services> servicesArrayList = new ArrayList<>();
    FragmentServicesBinding binding;
    NavController navController;
    ServicesAdapter servicesAdapter;
    Context context;
    String vehicleType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_services, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        ServicesFragmentArgs args = ServicesFragmentArgs.fromBundle(getArguments());
        vehicleType = args.getVehicleType();

        servicesArrayList.clear();
        switch (vehicleType) {
            case Global.VEHICLE_CAR:
                initListCar();
                break;
            case Global.VEHICLE_BIKE:
                initListBike();
                break;
            case Global.VEHICLE_RICKSHAW:
                initListRickshaw();
                break;
            case Global.VEHICLE_TRUCK:
                initListTruck();
                break;
        }

        actionViews();


    }

    private void initListTruck() {
        Services service = new Services();
        service.service = "General Inspection";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Truck Towing";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Brake Service";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Oil Change";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Filter Change";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Fuel Pump Replacement";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

    }

    private void initListRickshaw() {
        Services service = new Services();
        service.service = "General Inspection";
        service.vehicleType = Global.VEHICLE_RICKSHAW;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Basic Tuning";
        service.vehicleType = Global.VEHICLE_RICKSHAW;
        servicesArrayList.add(service);

        service = new Services();
        service.vehicleType = Global.VEHICLE_RICKSHAW;
        service.service = "Engine Repair";
        servicesArrayList.add(service);

        service = new Services();
        service.vehicleType = Global.VEHICLE_RICKSHAW;
        service.service = "Wiring";
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Clutch And Pressure Plates";
        service.vehicleType = Global.VEHICLE_RICKSHAW;
        servicesArrayList.add(service);

    }

    private void initListBike() {
        Services service = new Services();
        service.service = "General Inspection";
        service.vehicleType = Global.VEHICLE_BIKE;
        servicesArrayList.add(service);

        service = new Services();
        service.vehicleType = Global.VEHICLE_BIKE;
        service.service = "Basic Tuning";
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Head Repairing";
        service.vehicleType = Global.VEHICLE_BIKE;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Engine Repairing";
        service.vehicleType = Global.VEHICLE_BIKE;
        servicesArrayList.add(service);

        service = new Services();
        service.vehicleType = Global.VEHICLE_BIKE;
        service.service = "Wiring";
        servicesArrayList.add(service);

        service = new Services();
        service.vehicleType = Global.VEHICLE_BIKE;
        service.service = "Clutch And Pressure Plates";
        servicesArrayList.add(service);

        service = new Services();
        service.vehicleType = Global.VEHICLE_BIKE;
        service.service = "Engine Over Hauling";
        servicesArrayList.add(service);
    }

    private void initListCar() {
        Services service = new Services();
        servicesArrayList.clear();

        service.service = "General Inspection";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Engine Oil Change";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Brake Service";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Tank cleaning";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Fuel Pump Replacement";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Clutch set Replacement";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

        service = new Services();
        service.service = "Engine Over Hauling";
        service.vehicleType = Global.VEHICLE_CAR;
        servicesArrayList.add(service);

    }

    public void actionViews() {

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                servicesAdapter.getFilter().filter(newText);
                return false;
            }
        });

        context = getContext();
        servicesAdapter = new ServicesAdapter(context, servicesArrayList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        for (int i = 0; i < servicesArrayList.size(); i++) {
            Log.d(TAG, "actionViews: i = " + i + "  " + servicesArrayList.get(i).service);
        }
        binding.recyclerView.setAdapter(servicesAdapter);


        binding.nextBtn.setOnClickListener(v -> {
            Log.d(TAG, "actionViews: " + servicesAdapter.getCheckedServicesArrayList().size());

            Services[] x = new Services[servicesAdapter.getCheckedServicesArrayList().size()];

            for (int i = 0; i < x.length; i++) {
                x[i] = servicesAdapter.getCheckedServicesArrayList().get(i);
            }

            if (x.length > 0) {
                ServicesFragmentDirections.ActionServicesFragmentToScheduleBookingFragment action =
                        ServicesFragmentDirections.actionServicesFragmentToScheduleBookingFragment(x);

                action.setVehicleType(vehicleType);

                Navigation.createNavigateOnClickListener(action).onClick(binding.nextBtn);
            } else {
                Toast.makeText(context, "Please select a service", Toast.LENGTH_SHORT).show();
            }


        });

    }

}