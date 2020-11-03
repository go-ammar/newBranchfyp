package com.electrosoft.electrosoftnew.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.ListSensorBinding;
import com.electrosoft.electrosoftnew.models.Room;
import com.electrosoft.electrosoftnew.models.Sensor;
import com.electrosoft.electrosoftnew.models.Sensors;
import com.electrosoft.electrosoftnew.models.SensorsPerRoom;
import com.electrosoft.electrosoftnew.ui.GetSensorsFragment;
import com.electrosoft.electrosoftnew.ui.PinFragmentDirections;
import com.electrosoft.electrosoftnew.ui.SensorPerRoomFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class SensorPerRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<SensorsPerRoom> mdata;

    ArrayList<Sensors> mList;
    Context mContext;
    String id;

    public SensorPerRoomAdapter(ArrayList<Sensors> mdata, Context mContext, String id) {
        Log.d("TAG", "SensorPerRoomAdapter: "+mdata.size());
        this.mList = mdata;
        this.mContext = mContext;
        this.id = id;
    }



    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListSensorBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_sensor, parent, false);

        Log.d("TAG", "onCreateViewHolder: ");

        return new SensorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Log.d("TAG", "onBindViewHolder: "+mdata.get(position).name);

        Sensors sensorsPerRoom = mList.get(0);
        ((SensorViewHolder) holder ).setBinding(sensorsPerRoom);



//        Log.d("TAG", "onBindViewHolder: size of sensor list is " +mList.size()+" at some place "+mList.get(position));
        if (mList.get(0).data.size()!=0){
//            for (int i =0; i < mList.get(position).data.size(); i++){
//                ((SensorViewHolder) holder ).binding.roomNameTextview.setText(mList.get(position).data.get(i).name);
//            }
            ((SensorViewHolder) holder ).binding.roomNameTextview.setText(mList.get(0).data.get(position).name);
//            ((SensorViewHolder) holder ).binding.descriptionTextview.setVisibility(View.GONE);


        } else {
            ((SensorViewHolder) holder ).binding.rm1.setVisibility(View.GONE);
            Toast toast = Toast.makeText(mContext, "No Devices for room", Toast.LENGTH_SHORT);
            toast.show();
        }

        ((SensorViewHolder) holder ).binding.stats.setOnClickListener(view -> {



            SensorPerRoomFragmentDirections.ActionSensorPerRoomFragmentToStatisticsFragment action =
                    SensorPerRoomFragmentDirections.actionSensorPerRoomFragmentToStatisticsFragment();

            action.setSensorDevice(mList.get(0).data.get(position).name);
            action.setId(id);
            Navigation.createNavigateOnClickListener(action).onClick(((SensorViewHolder) holder).binding.stats);


//
//            Navigation.createNavigateOnClickListener(R.id.action_sensorPerRoomFragment_to_statisticsFragment).
//                    onClick(((SensorViewHolder) holder).binding.stats);

        });

    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount: "+mList.size());
        return mList.get(0).data.size();
    }

    public static class SensorViewHolder extends RecyclerView.ViewHolder{

        ListSensorBinding binding;


        public SensorViewHolder(ListSensorBinding binding) {
            super( binding.getRoot() );
            Log.d("TAG", "SensorViewHolder: ");
            this.binding = binding;
        }

        void setBinding(Sensors sensor){
            binding.setSensor(sensor);
            binding.executePendingBindings();
        }
    }
}
