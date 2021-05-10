package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ItemMechAccBinding;
import com.fypapplication.fypapp.databinding.ItemMyServicesBinding;
import com.fypapplication.fypapp.models.MechMyAccount;
import com.fypapplication.fypapp.models.MechServices;

import java.util.ArrayList;

public class MechMyAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<MechMyAccount> mechMyAccountArrayList;
    MechMyAccountInterface myAccountInterface;
    Context context;

    public MechMyAccountAdapter(Context context, ArrayList<MechMyAccount> mechMyAccountArrayList, MechMyAccountInterface myAccountInterface) {
        this.mechMyAccountArrayList = mechMyAccountArrayList;
        this.myAccountInterface = myAccountInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public MechAccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMechAccBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_mech_acc, parent, false);

        return new MechAccViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MechMyAccount service = mechMyAccountArrayList.get(position);
        ((MechAccViewHolder) holder).setBinding(service);

        ((MechAccViewHolder) holder).binding.deleteChangeDue.setOnClickListener(v -> {
            myAccountInterface.deleteAcc(service);
        });
    }

    @Override
    public int getItemCount() {
        return mechMyAccountArrayList.size();
    }

    public static class MechAccViewHolder extends RecyclerView.ViewHolder {

        ItemMechAccBinding binding;

        public MechAccViewHolder(@NonNull ItemMechAccBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(MechMyAccount service) {

            binding.setService(service);
            binding.executePendingBindings();

        }
    }

    public interface MechMyAccountInterface{
        void deleteAcc(MechMyAccount service);
    }

}
