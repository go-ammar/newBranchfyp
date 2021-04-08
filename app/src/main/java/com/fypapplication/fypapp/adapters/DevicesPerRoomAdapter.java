package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ListDevicesBinding;
import com.fypapplication.fypapp.models.DeviceModelClass;
import com.fypapplication.fypapp.ui.DevicePerRoomFragmentDirections;

import java.util.ArrayList;

public class DevicesPerRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<DeviceModelClass> mList;
    Context mContext;
    String id;


    public DevicesPerRoomAdapter(ArrayList<DeviceModelClass> mdata, Context mContext, String id) {
        Log.d("TAG", "SensorPerRoomAdapter: " + mdata.size());
        this.mList = mdata;
        this.mContext = mContext;
        this.id = id;

    }


    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListDevicesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_devices, parent, false);

        Log.d("TAG", "onCreateViewHolder: ");

        return new SensorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        DeviceModelClass sensorsPerRoom = mList.get(position);
        ((SensorViewHolder) holder).setBinding(sensorsPerRoom);



        ((SensorViewHolder) holder).binding.roomNameTextview.setText(mList.get(position).deviceName);

        ((SensorViewHolder) holder).binding.stats.setText("See Sensors");

        ((SensorViewHolder) holder).binding.stats.setOnClickListener(view -> {

            DevicePerRoomFragmentDirections.ActionDevicePerRoomFragmentToSensorsFragment actions =
                    DevicePerRoomFragmentDirections.actionDevicePerRoomFragmentToSensorsFragment();

            actions.setDeviceId(mList.get(position).id);
            actions.setDeviceName(mList.get(position).deviceName);

            Navigation.createNavigateOnClickListener(actions).
                    onClick(((SensorViewHolder) holder).binding.stats);

        });

    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount: " + mList.size());
        return mList.size();
    }

    public static class SensorViewHolder extends RecyclerView.ViewHolder {

        ListDevicesBinding binding;


        public SensorViewHolder(ListDevicesBinding binding) {
            super(binding.getRoot());
            Log.d("TAG", "SensorViewHolder: ");
            this.binding = binding;
        }

        void setBinding(DeviceModelClass sensor) {
            binding.executePendingBindings();
        }
    }
}
