package com.fypapplication.fypapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ItemServiceBinding;
import com.fypapplication.fypapp.models.Services;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final String TAG = "ServicesAdapter";
    private final Context context;
    private final ArrayList<Services> servicesArrayList;
    private final ArrayList<Services> checkedServicesArrayList = new ArrayList<>();
    private ArrayList<Services> filteredArrayList;

    public ServicesAdapter(Context context, ArrayList<Services> servicesArrayList) {
        this.context = context;
        this.servicesArrayList = servicesArrayList;
        filteredArrayList = servicesArrayList;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemServiceBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_service, parent, false);

        return new ServicesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Services service = filteredArrayList.get(position);
        ((ServicesViewHolder) holder).setBinding(service);

        Log.d(TAG, "onBindViewHolder: " + service.service);

        ((ServicesViewHolder) holder).binding.serviceTv.setOnClickListener(v -> {
            ((ServicesViewHolder) holder).binding.checkbox.setChecked(!((ServicesViewHolder) holder).binding.checkbox.isChecked());
        });

        ((ServicesViewHolder) holder).binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedServicesArrayList.add(filteredArrayList.get(position));
            } else {
                checkedServicesArrayList.remove(filteredArrayList.get(position));
            }
        });


    }

    public ArrayList<Services> getCheckedServicesArrayList() {
        return checkedServicesArrayList;
    }

    @Override
    public int getItemCount() {
//        return servicesArrayList.size();
        return filteredArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filteredArrayList = servicesArrayList;
                } else {
                    ArrayList<Services> filterList = new ArrayList<>();
                    for (Services i : servicesArrayList) {
                        if (i.service.toLowerCase().contains(charString.toLowerCase())) {
                            filterList.add(i);
                        }

                    }
                    filteredArrayList = filterList;
                }

                Filter.FilterResults filterResults = new FilterResults();
                filterResults.values = filteredArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredArrayList = (ArrayList<Services>) results.values;

                notifyDataSetChanged();

            }
        };
    }


    public static class ServicesViewHolder extends RecyclerView.ViewHolder {

        ItemServiceBinding binding;

        public ServicesViewHolder(@NonNull ItemServiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(Services service) {

            binding.setService(service);
            binding.executePendingBindings();

        }
    }
}
