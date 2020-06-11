package com.electrosoft.electrosoftnew.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.electrosoft.electrosoftnew.Models.RoomModel;
import com.electrosoft.electrosoftnew.databinding.FragmentGetRoomsBinding;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    FragmentGetRoomsBinding binding;
    private Context mContext;
    private List<RoomModel> mdata;

    public RoomAdapter(Context mContext, List<RoomModel> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    }




    @NonNull
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {

    }

    public int getItemCount() {
        return 0;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{

        public RoomViewHolder(@NonNull View itemView) {
            super( itemView );
        }


    }

}
