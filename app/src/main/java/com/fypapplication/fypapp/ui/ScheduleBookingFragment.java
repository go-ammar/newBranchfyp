package com.fypapplication.fypapp.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentScheduleBookingBinding;
import com.fypapplication.fypapp.models.Services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleBookingFragment extends Fragment {


    FragmentScheduleBookingBinding binding;
    NavController navController;
    DatePickerDialog picker;
    String dateString = null;
    private List<Services> servicesArrayList = new ArrayList<>();
    private static final String TAG = "ScheduleBookingFragment";
    int hour;
    int min;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_booking, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        ScheduleBookingFragmentArgs args = ScheduleBookingFragmentArgs.fromBundle(getArguments());
        servicesArrayList = Arrays.asList(args.getService());

        actionViews();
    }

    private void actionViews() {

        binding.dateFromEt.setOnClickListener(v -> {
            showDatePicker();
        });

        binding.nextBtn.setOnClickListener(v -> {

        });
        hour = binding.timePicker1.getCurrentHour();
        min = binding.timePicker1.getCurrentMinute();

        binding.proceedbtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MapMarkerActivity.class);
            startActivity(intent);

        });

    }

    private void showDatePicker() {
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

                    binding.dateFromEt.setText(dateString);

                }, year, month, day);

        picker.show();

    }

}