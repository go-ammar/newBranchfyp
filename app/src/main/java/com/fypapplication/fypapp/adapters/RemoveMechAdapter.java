package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ItemMechlistBinding;
import com.fypapplication.fypapp.databinding.ItemMyServicesBinding;
import com.fypapplication.fypapp.models.MechServices;
import com.fypapplication.fypapp.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RemoveMechAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RemoveMechAdapter";
    Context context;
    ArrayList<User> userArrayList;
    RemoveMechInterface removeMechInterface;
    ArrayList<String> address;


    public RemoveMechAdapter(Context context, ArrayList<User> userArrayList, RemoveMechInterface removeMechInterface, ArrayList<String> address) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.removeMechInterface = removeMechInterface;
        this.address = address;
    }

    @NonNull
    @Override
    public RemoveMechViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMechlistBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_mechlist, parent, false);


        return new RemoveMechViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = userArrayList.get(position);

//        String address = getAddress(Long.parseLong(user.lat), Long.parseLong(user.lng));
        String address = this.address.get(position);
        ((RemoveMechViewHolder) holder).setBinding(user, address);
        Log.d(TAG, "onBindViewHolder: "+user.id);

        ((RemoveMechViewHolder) holder).binding.delete.setOnClickListener(v -> {
            removeMechInterface.removeMech(userArrayList.get(position));
            Log.d(TAG, "onBindViewHolder: "+userArrayList.get(position).id);
        });

    }


    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public interface RemoveMechInterface {
        void removeMech(User user);
    }

    public static class RemoveMechViewHolder extends RecyclerView.ViewHolder {

        ItemMechlistBinding binding;

        public RemoveMechViewHolder(@NonNull ItemMechlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(User user, String address) {

            binding.setUser(user);
            binding.setAddress(address);
            binding.executePendingBindings();

        }
    }
}
