package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ItemMechServicesBinding;
import com.fypapplication.fypapp.databinding.ItemMyServicesBinding;
import com.fypapplication.fypapp.models.MechServices;

import java.util.ArrayList;

public class MechanicPriceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<MechServices> mechPriceArraylist;
    MechanicPriceAdapter.MyServicesInterface myServicesInterface;

    public MechanicPriceAdapter(Context context, ArrayList<MechServices> mechPriceArraylist, MechanicPriceAdapter.MyServicesInterface myServicesInterface) {
        this.context = context;
        this.mechPriceArraylist = mechPriceArraylist;
        this.myServicesInterface = myServicesInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMechServicesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_mech_services, parent, false);

        return new MechanicPriceAdapter.MechServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MechServices service = mechPriceArraylist.get(position);
        ((MechanicPriceAdapter.MechServiceViewHolder) holder).setBinding(service);

        ((MechServiceViewHolder) holder).binding.serviceTv.setOnClickListener(v -> {
            myServicesInterface.onClickService(service);
        });

    }

    @Override
    public int getItemCount() {
        return mechPriceArraylist.size();
    }

    public interface MyServicesInterface {
        void onClickService(MechServices mechServices);
    }

    public static class MechServiceViewHolder extends RecyclerView.ViewHolder {

        ItemMechServicesBinding binding;

        public MechServiceViewHolder(@NonNull ItemMechServicesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(MechServices service) {

            binding.setService(service);
            binding.executePendingBindings();

        }
    }
}
