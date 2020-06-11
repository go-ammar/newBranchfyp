package com.electrosoft.electrosoftnew.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.electrosoft.electrosoftnew.Models.RoomModel;
import com.electrosoft.electrosoftnew.Models.SensorModel;
import com.electrosoft.electrosoftnew.databinding.FragmentGetRoomsBinding;
import com.electrosoft.electrosoftnew.databinding.FragmentGetSensorsBinding;

import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {



    private Context mContext;
    private List<SensorModel> mdata;


    public SensorAdapter(Context mContext, List<SensorModel> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    }




    @NonNull
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {

    }

    public int getItemCount() {
        return 0;
    }

    public class SensorViewHolder extends RecyclerView.ViewHolder{
        // binding;


        public SensorViewHolder(@NonNull View itemView) {
            super( itemView );
        }


    }

}
