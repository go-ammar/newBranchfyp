package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.Interfaces.RoomInterface;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ListGetroomBinding;
import com.fypapplication.fypapp.models.GetRoom;
import com.fypapplication.fypapp.ui.GetRoomsFragmentDirections;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private Context mContext;

    private List<GetRoom> getRoomList;
    private RoomInterface roomInterface;


    public RoomAdapter(Context mContext, List<GetRoom> mdata, RoomInterface roomInterface) {
        this.mContext = mContext;
        this.getRoomList = mdata;
        this.roomInterface = roomInterface;
    }


    @NonNull
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListGetroomBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_getroom, parent, false);

        return new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GetRoom room = getRoomList.get(0);
        ((RoomViewHolder) holder).setBinding(room);

        ((RoomViewHolder) holder).binding.roomNameTextview.setText(getRoomList.get(0).data.get(position).name);

        ((RoomViewHolder) holder).binding.updateroom.setOnClickListener(v -> {
            roomInterface.updateDialog(position);

        });


        ((RoomViewHolder) holder).binding.deletesensor.setOnClickListener(v -> {
            roomInterface.deleteDialog(position);

        });

        ((RoomViewHolder) holder).binding.viewallsensor.setOnClickListener(v -> {


            GetRoomsFragmentDirections.ActionNavGetRoomsToSensorPerRoomFragment action =
                    GetRoomsFragmentDirections.actionNavGetRoomsToSensorPerRoomFragment();

            action.setId(getRoomList.get(0).data.get(position).id);
            action.setRoomName(getRoomList.get(0).data.get(position).name);

            Navigation.createNavigateOnClickListener(action).
                    onClick(((RoomViewHolder) holder).binding.viewallsensor);

            getRoomList.clear();

        });


    }


    public int getItemCount() {
        return getRoomList.get(0).data.size();
    }


    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        ListGetroomBinding binding;


        public RoomViewHolder(ListGetroomBinding binding) {

            super(binding.getRoot());
            this.binding = binding;
        }

        void setBinding(GetRoom room) {
//            binding.setRoom(room);
            binding.executePendingBindings();
        }


    }

}
