package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ItemChangesBinding;
import com.fypapplication.fypapp.databinding.ItemServiceBinding;
import com.fypapplication.fypapp.models.ChangesDue;

import java.util.ArrayList;

public class ChangesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ChangesDue> changesDueArrayList;
    ChangeInterface changeInterface;
    private static final String TAG = "ChangesAdapter";

    public ChangesAdapter(Context context, ArrayList<ChangesDue> changesDues, ChangeInterface changeInterface) {
        this.context = context;
        this.changesDueArrayList = changesDues;
        this.changeInterface = changeInterface;

    }

    @NonNull
    @Override
    public ChangesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemChangesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_changes, parent, false);

        return new ChangesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ChangesDue changesDue = changesDueArrayList.get(position);
        ((ChangesViewHolder) holder).setBinding(changesDue);

        ((ChangesViewHolder) holder).binding.deleteChangeDue.setOnClickListener(v -> {
            changeInterface.deleteService(changesDue);
        });

    }

    @Override
    public int getItemCount() {
        return changesDueArrayList.size();
    }

    public interface ChangeInterface {
        void deleteService(ChangesDue changesDue);
    }

    public static class ChangesViewHolder extends RecyclerView.ViewHolder {

        ItemChangesBinding binding;

        public ChangesViewHolder(@NonNull ItemChangesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(ChangesDue service) {

            binding.setService(service);
            binding.executePendingBindings();

        }
    }

}
