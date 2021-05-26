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
import android.widget.Toast;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentScheduleBookingBinding;
import com.fypapplication.fypapp.models.Services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleBookingFragment extends Fragment {


    private static final String TAG = "ScheduleBookingFragment";
    FragmentScheduleBookingBinding binding;
    NavController navController;
    DatePickerDialog picker;
    String dateString = null;
    int hour;
    int min;
    private final List<Services> servicesArrayList = new ArrayList<>();

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

            if (dateString == null){
                Toast.makeText(getContext(), "Please set a date", Toast.LENGTH_SHORT).show();
            }else {
                dateString = dateString + " "+ hour + ":" + min +":00";
//                11/10/2017 11:16:46

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                //Date/time pattern of desired output date
                DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                Date date = null;
                String output = null;
                try{
                    //Conversion of input String to date
                    date= df.parse(dateString);
                    //old date format to new date format
                    output = outputformat.format(date);
                    Log.d(TAG, "actionViews: date2   "+output);
                }catch(ParseException pe){
                    pe.printStackTrace();
                }


                Log.d(TAG, "actionViews: " + dateString);

                try {
                    ScheduleBookingFragmentDirections.ActionScheduleBookingFragmentToMapsFragment action =
                            ScheduleBookingFragmentDirections.actionScheduleBookingFragmentToMapsFragment();

                    ScheduleBookingFragmentArgs args = ScheduleBookingFragmentArgs.fromBundle(getArguments());

                    action.setService(args.getService());
                    action.setFromServices(true);
                    action.setDate(output);

                    navController.navigate(action);
                } catch (Exception e) {
                    Log.e(TAG, "actionViews: ", e);
                }
            }

        });

    }

    private void showDatePicker() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
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