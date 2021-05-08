package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ItemMyServicesBinding;
import com.fypapplication.fypapp.models.ChangesDue;
import com.fypapplication.fypapp.models.MechServices;

import java.util.ArrayList;

public class MechServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<MechServices> mechServicesArrayList;
    MyServicesInterface myServicesInterface;

    public MechServicesAdapter(Context context, ArrayList<MechServices> mechServicesArrayList, MyServicesInterface myServicesInterface) {
        this.context = context;
        this.mechServicesArrayList = mechServicesArrayList;
        this.myServicesInterface = myServicesInterface;
    }

    @NonNull
    @Override
    public MechServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMyServicesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_my_services, parent, false);

        return new MechServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MechServices service = mechServicesArrayList.get(position);
        ((MechServiceViewHolder) holder).setBinding(service);

        ((MechServiceViewHolder) holder).binding.deleteChangeDue.setOnClickListener(v -> {
            myServicesInterface.deleteService(service);
        });

    }

    @Override
    public int getItemCount() {
        return mechServicesArrayList.size();
    }

    public interface MyServicesInterface {
        void deleteService(MechServices mechServices);
    }

    public static class MechServiceViewHolder extends RecyclerView.ViewHolder {

        ItemMyServicesBinding binding;

        public MechServiceViewHolder(@NonNull ItemMyServicesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(MechServices service) {

            binding.setService(service);
            binding.executePendingBindings();

        }
    }

}
