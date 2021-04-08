package com.fypapplication.fypapp.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.YourMarkerView;
import com.fypapplication.fypapp.databinding.FragmentStatisticsBinding;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static java.lang.System.currentTimeMillis;


public class StatisticsFragment extends Fragment {

    FragmentStatisticsBinding binding;
    String TAG = "Stats";
    Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false);
        context = getContext();
        return binding.getRoot();

    }

    @Override
    public void onPause() {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actionViews();
    }


    @Override
    public void onResume() {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onResume();
    }

    private void actionViews() {

        assert getArguments() != null;
        StatisticsFragmentArgs SensorDeviceDetails = StatisticsFragmentArgs.fromBundle(getArguments());
        String SensorName = SensorDeviceDetails.getSensorName();
        String id = SensorDeviceDetails.getSensorId();

        binding.tv.setText(SensorName);

        SharedPrefs sharedPrefs = new SharedPrefs(context);
        JSONObject params = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebServices.API_GET_SENSORS + id, params, response -> {

            Log.d("Stats", "Stats: res " + response);

            ArrayList<Entry> entries = new ArrayList<>();


            JSONObject objnew = new JSONObject();

            try {
                JSONArray arr = response.getJSONArray("data");

                Log.d(TAG, "actionViewsstats: " + arr.length());


                int j = 0;

                for (int i = 0; arr.length() > i; i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    if (obj.get("name").equals(SensorName)) {
                        JSONArray jsonArray = obj.getJSONArray("readings");

                        if (jsonArray.length() == 0) {
                            binding.tv2.setVisibility(View.VISIBLE);
                            binding.progress.setVisibility(View.GONE);
                            binding.linechart.setVisibility(View.GONE);
                        } else {
                            binding.linechart.setVisibility(View.VISIBLE);
                        }

                        for (j = 0; jsonArray.length() > j; j++) {
                            objnew = jsonArray.getJSONObject(j);

                            String input = objnew.get("updatedAt").toString();

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            Date date = null;
                            try {
                                date = dateFormat.parse(input);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            long unixTime = (long) date.getTime() / 1000;

                            Log.d(TAG, "actionViews: unix " + unixTime);


                            Log.d(TAG, "actionViews: output " + date);
                            entries.add(new Entry((float) date.getTime(),
                                    Float.parseFloat(objnew.get("value").toString())));

                        }
                    }
                }

                Log.d(TAG, "actionViews1: cur " + currentTimeMillis());


                LineDataSet lineDataSet = new LineDataSet(entries, SensorName);
                lineDataSet.setColor(Color.WHITE);
                lineDataSet.setValueTextSize(7);
                lineDataSet.setDrawValues(false);
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setValueTextColor(Color.WHITE);
                ArrayList<ILineDataSet> dataSet = new ArrayList<>();
                dataSet.add(lineDataSet);

                binding.linechart.getLegend().setTextColor(Color.WHITE);
                LineData data = new LineData(dataSet);

                binding.linechart.setDrawGridBackground(false);

                binding.linechart.setDrawBorders(true);
                binding.linechart.setBorderWidth(1f);


                binding.linechart.setBorderColor(Color.WHITE);
                binding.linechart.getDescription().setTextColor(Color.WHITE);
                binding.linechart.getDescription().setText(SensorName + "/Time");
                binding.linechart.getDescription().setTextSize(12f);
                binding.linechart.setTouchEnabled(true);
                binding.linechart.setDragEnabled(true);


                XAxis xAxis = binding.linechart.getXAxis();
                xAxis.setCenterAxisLabels(true);


                binding.linechart.getXAxis().setDrawLabels(false);

                binding.linechart.getAxisLeft().setTextColor(Color.WHITE);
                binding.linechart.getAxisRight().setTextColor(Color.WHITE);
                binding.linechart.getXAxis().setTextColor(Color.WHITE);

                binding.linechart.setScaleEnabled(true);
                binding.linechart.setPinchZoom(true);

                binding.progress.setVisibility(View.GONE);


                binding.linechart.setData(data);

                Log.d(TAG, "actionViewsStats: " + binding.linechart.getXAxis().getAxisMinimum() + " max: " +
                        binding.linechart.getXAxis().getAxisMaximum());
//                binding.linechart.setVisibleXRangeMaximum(10000000);
                binding.linechart.animateX(1000);

                YourMarkerView mv = new YourMarkerView(context, R.layout.tvcontent);

                binding.linechart.setMarker(mv);
                binding.linechart.invalidate();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            binding.progress.setVisibility(View.GONE);
            Toast toast = Toast.makeText(context, "Could not get Readings", Toast.LENGTH_SHORT);
            toast.show();
            Log.d("Stats", "_apiGetSensors: error " + error);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap header = new HashMap<>();
                header.put("Authorization", sharedPrefs.getKey());
                Log.d("TAG", "getHeaders: " + header.toString());
                return header;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }

    private class MyAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {

            Date date = new Date((long) (value * 1000L));
            // format of the date
            SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
//            String java_date = jdf.format(date);

            return jdf.format(date);
        }
    }

}


