package com.electrosoft.electrosoftnew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.GetroomdesignBinding;
import com.electrosoft.electrosoftnew.models.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<Room> mdata;

    public RoomAdapter(Context mContext, List<Room> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    }




    @NonNull
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GetroomdesignBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.getroomdesign,parent,false);
        return new RoomViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Room room = mdata.get(position);
        ((RoomViewHolder)holder).setBinding(room);

        ((RoomViewHolder) holder).binding.updateroom.setOnClickListener( v -> {

        });
        ((RoomViewHolder) holder).binding.addsensor.setOnClickListener( v -> {

        });

        ((RoomViewHolder) holder).binding.viewallsensor.setOnClickListener(  v -> {

        });




    }

    public int getItemCount() {
        return 0;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder{

        GetroomdesignBinding binding;


        public RoomViewHolder(GetroomdesignBinding binding) {

            super( binding.getRoot() );
            this.binding = binding;
        }

        void setBinding(Room room){
            binding.setRoom(room);
            binding.executePendingBindings();
        }


    }

}
