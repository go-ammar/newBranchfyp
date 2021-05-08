package com.fypapplication.fypapp.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.ChangesAdapter;
import com.fypapplication.fypapp.databinding.AddChangeDialogBinding;
import com.fypapplication.fypapp.databinding.FragmentContactBinding;
import com.fypapplication.fypapp.helper.ChangesHelper;
import com.fypapplication.fypapp.models.ChangesDue;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.google.android.gms.maps.GoogleMap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class ContactFragment extends Fragment implements ChangesAdapter.ChangeInterface {

    private static final String TAG = "ContactFragment";
    NavController navController;
    FragmentContactBinding binding;
    Context mContext;
    DatePickerDialog picker;
    String dateString = null;
    ChangesHelper changesHelper;
    SharedPrefs sharedPrefs;
    ChangesAdapter changesAdapter;
    ArrayList<ChangesDue> changesDueArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        mContext = getContext();
        actionViews();
    }

    private void actionViews() {

        sharedPrefs = new SharedPrefs(mContext);
        changesHelper = new ChangesHelper(mContext);


        changesDueArrayList = sharedPrefs.getChangesList();
        changesAdapter = new ChangesAdapter(mContext, changesDueArrayList, this);
        binding.changesRecyclerView.setAdapter(changesAdapter);

        binding.changesRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL));

        binding.addChangebtn.setOnClickListener(v -> {

            addServiceDialog();

        });

//        binding.fab1.setOnClickListener(view -> {
//
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            intent.setData(Uri.parse("tel:03402211539"));
//            startActivity(intent);
//
//        });


    }

    private void addServiceDialog() {
        AddChangeDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.add_change_dialog,
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
                    (view, year1, monthOfYear, dayOfMonth) -> {

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


        binding1.addRoomBtn.setOnClickListener(v -> {

            if (!binding1.durationOfChangeET.getText().toString().isEmpty() && !dateString.isEmpty()
                    && !binding1.serviceNameET.getText().toString().isEmpty()) {

                ChangesDue changesDue = new ChangesDue();
                changesDue.dateOfChange = dateString;
                changesDue.duration = Integer.parseInt(Objects.requireNonNull(binding1.durationOfChangeET.getText()).toString());
                changesDue.typeOfChange = Objects.requireNonNull(binding1.serviceNameET.getText()).toString();
                changesDueArrayList.add(changesDue);
                sharedPrefs.putChangesList(changesDueArrayList);

                alertDialog.dismiss();
                changesAdapter.notifyDataSetChanged();

            }
        });

        alertDialog.show();

    }


    @Override
    public void deleteService(ChangesDue changesDue) {

        for (int i = 0; i < changesDueArrayList.size(); i++) {
            ChangesDue cd = changesDueArrayList.get(i);

            if (cd.typeOfChange.equals(changesDue.typeOfChange) && cd.dateOfChange.equals(changesDue.dateOfChange)) {
                changesDueArrayList.remove(i);
            }
        }
        sharedPrefs.putChangesList(changesDueArrayList);
        changesAdapter.notifyDataSetChanged();
    }
}