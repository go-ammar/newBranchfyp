package com.electrosoft.electrosoftnew.adapters;

import android.content.Context;
import android.hardware.Sensor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.ListDevicesBinding;
import com.electrosoft.electrosoftnew.models.DeviceModelClass;
import com.electrosoft.electrosoftnew.models.Sensors;
import com.electrosoft.electrosoftnew.ui.DevicePerRoomFragmentDirections;
import com.electrosoft.electrosoftnew.ui.SensorsFragmentDirections;

import java.util.ArrayList;

import static java.util.Objects.isNull;

public class SensorsPerDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //    ArrayList<DeviceModelClass> mList;
    ArrayList<Sensors> mList = new ArrayList<>();

    Context mContext;
    String id;

    private static final String TAG = "SensorsPerDeviceAdapter";

    public SensorsPerDeviceAdapter(ArrayList<Sensors> mdata, Context mContext) {
        this.mList = mdata;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public SensorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListDevicesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_devices, parent, false);


        return new SensorsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Sensors sensors = mList.get(0);
        ((SensorsViewHolder) holder).setBinding(sensors);

        ((SensorsViewHolder) holder).binding.roomNameTextview.setText(mList.get(0).data.get(position).name);

        ((SensorsViewHolder) holder).binding.stats.setText("See Statistics");


        ((SensorsViewHolder) holder).binding.stats.setOnClickListener(v -> {

            SensorsFragmentDirections.ActionSensorsFragmentToStatisticsFragment action =
                    SensorsFragmentDirections.actionSensorsFragmentToStatisticsFragment();

            action.setSensorId(mList.get(0).data.get(position).device);
            action.setSensorName(mList.get(0).data.get(position).name);

            Navigation.createNavigateOnClickListener(action).
                    onClick(((SensorsViewHolder) holder).binding.stats);

//            NavDirections action =
//                    DevicePerRoomFragmentDirections
//                            .actionDevicePerRoomFragmentToSensorsFragment();
//            Navigation.findNavController(view).navigate(action);

        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + mList.get(0).data.size());
        if (!isNull(mList.get(0)))
            return mList.get(0).data.size();
        return 0;
    }

    public static class SensorsViewHolder extends RecyclerView.ViewHolder {

        ListDevicesBinding binding;


        public SensorsViewHolder(ListDevicesBinding binding) {
            super(binding.getRoot());
            Log.d("TAG", "SensorViewHolder: ");
            this.binding = binding;
        }

        void setBinding(Sensors sensor) {
            binding.executePendingBindings();
        }
    }
}
