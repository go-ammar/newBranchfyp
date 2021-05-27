package com.fypapplication.fypapp.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.MechMyAccountAdapter;
import com.fypapplication.fypapp.databinding.AddMechAccDialogBinding;
import com.fypapplication.fypapp.databinding.AddServiceDialogBinding;
import com.fypapplication.fypapp.databinding.FragmentMyAccountBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.ChangesDue;
import com.fypapplication.fypapp.models.MechMyAccount;
import com.fypapplication.fypapp.models.MechServices;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MyAccountFragment extends Fragment implements MechMyAccountAdapter.MechMyAccountInterface {

    private static final String TAG = "MyAccountFragment";
    FragmentMyAccountBinding binding;
    Context context;
    SharedPrefs sharedPrefs;
    ArrayList<MechMyAccount> mechMyAccountArrayList;
    DatePickerDialog picker;
    String dateString;
    MechMyAccountAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        sharedPrefs = new SharedPrefs(context);
        mechMyAccountArrayList = new ArrayList<>();
        actionViews();

    }

    private void actionViews() {

        mechMyAccountArrayList = sharedPrefs.getMechMyAccount();

        adapter = new MechMyAccountAdapter(context, mechMyAccountArrayList, this);
        binding.changesRecyclerView.setAdapter(adapter);

        getTotal();

        binding.addCostbtn.setOnClickListener(view -> {
            AddMechAccDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.add_mech_acc_dialog,
                    null, false);

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setView(binding1.getRoot())
                    .create();

            binding1.dateOfChangeET.setOnClickListener(v -> {
                SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", Locale.US);
                Calendar calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                picker = new DatePickerDialog(requireContext(),
                        (views, year1, monthOfYear, dayOfMonth) -> {

                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(year1, monthOfYear, dayOfMonth);
                            dateString = format.format(calendar1.getTime());

                            binding1.dateOfChangeET.setText(dateString);

                        }, year, month, day);

                picker.show();
            });

            binding1.CancelBtn.setOnClickListener(v -> {
                alertDialog.dismiss();
            });

            binding1.addCostbtn.setOnClickListener(v -> {

                if (!Objects.requireNonNull(binding1.customerNameET.getText()).toString().isEmpty() && !Objects.requireNonNull(binding1.phoneNumberET.getText()).toString().isEmpty()
                        && !Objects.requireNonNull(binding1.dateOfChangeET.getText()).toString().isEmpty() && !Objects.requireNonNull(binding1.descriptionET.getText()).toString().isEmpty()
                        && !Objects.requireNonNull(binding1.costET.getText()).toString().isEmpty()) {

                    mechMyAccountArrayList = sharedPrefs.getMechMyAccount();

                    MechMyAccount mechServices = new MechMyAccount();
                    mechServices.cost = Integer.parseInt(binding1.costET.getText().toString());

                    mechServices.name = binding1.customerNameET.getText().toString();
                    mechServices.date = binding1.dateOfChangeET.getText().toString();
                    mechServices.description = binding1.descriptionET.getText().toString();
                    mechServices.phoneNumber = binding1.phoneNumberET.getText().toString();

                    mechMyAccountArrayList.add(mechServices);

                    sharedPrefs.putMechAcountList(mechMyAccountArrayList);
                    adapter.notifyDataSetChanged();

                    alertDialog.dismiss();

                }
            });

            alertDialog.show();
        });


    }

    private void getTotal() {

        int cost = 0;
        for (int i = 0; i < mechMyAccountArrayList.size(); i++) {
            cost += mechMyAccountArrayList.get(i).cost;
        }

        binding.setTotal(String.valueOf(cost));

    }

    @Override
    public void deleteAcc(MechMyAccount service) {

        mechMyAccountArrayList = sharedPrefs.getMechMyAccount();

        for (int i = 0; i < mechMyAccountArrayList.size(); i++) {
            MechMyAccount cd = mechMyAccountArrayList.get(i);

            if (cd.name.equals(service.name) && cd.description.equals(service.description) && cd.date.equals(service.date)) {
                mechMyAccountArrayList.remove(i);
            }
        }

        sharedPrefs.putMechAcountList(mechMyAccountArrayList);
        adapter.notifyDataSetChanged();

    }

}
