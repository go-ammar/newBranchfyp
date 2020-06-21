package com.electrosoft.electrosoftnew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.GetsensordesignBinding;
import com.electrosoft.electrosoftnew.models.SensorModel;

import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<SensorModel> mdata;


    public SensorAdapter(Context mContext, List<SensorModel> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    }




    @NonNull
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GetsensordesignBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.getsensordesign,parent,false);
        return new SensorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SensorModel sensorModel = mdata.get(position);
        ((SensorViewHolder)holder).setBinding(sensorModel);

        ((SensorViewHolder) holder).binding.viewhistory.setOnClickListener( v -> {

        } );
        ((SensorViewHolder) holder).binding.updatesensor.setOnClickListener(v -> {

                });

    }


    public int getItemCount() {
        return 0;
    }

    public class SensorViewHolder extends RecyclerView.ViewHolder{

        GetsensordesignBinding binding;

        public SensorViewHolder(GetsensordesignBinding binding) {
            super( binding.getRoot() );
            this.binding = binding;

        }

        void setBinding(SensorModel sensorModel){
            binding.setSensormodel(sensorModel);
            binding.executePendingBindings();
        }


    }

}
