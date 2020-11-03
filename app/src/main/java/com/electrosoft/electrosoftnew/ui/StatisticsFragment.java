package com.electrosoft.electrosoftnew.ui;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.FragmentStatisticsBinding;
import com.electrosoft.electrosoftnew.models.Sensors;
import com.electrosoft.electrosoftnew.sharedprefs.SharedPrefs;
import com.electrosoft.electrosoftnew.webservices.VolleySingleton;
import com.electrosoft.electrosoftnew.webservices.WebServices;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class StatisticsFragment extends Fragment {

    FragmentStatisticsBinding binding;
    NavController navController;
    private String URL;
    String TAG = "Stats";


    ArrayList<Entry> x = new ArrayList<>();
    ArrayList<String> y = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false);
        return binding.getRoot();

    }

    @Override
    public void onPause() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progress.setVisibility(View.VISIBLE);
        binding.linechart.setVisibility(View.GONE);
        actionViews();
    }


//    public static Integer tsToSec8601(String timestamp) {
//        if (timestamp == null) return 1;
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//            Date dt = sdf.parse(timestamp);
//            long epoch = dt.getTime();
//            return (int) (epoch / 1000);
//        } catch (ParseException e) {
//            return 2;
//        }
//    }


    private void actionViews() {


        StatisticsFragmentArgs SensorDeviceDetails = StatisticsFragmentArgs.fromBundle(getArguments());
        String SensorName = SensorDeviceDetails.getSensorDevice();
        String id = SensorDeviceDetails.getId();

        binding.tv.setText(SensorName);

        SharedPrefs sharedPrefs = new SharedPrefs(getContext());
        JSONObject params = new JSONObject();

        //TODO remove this test id and add id in its place
//        String testId = "5f637ce0784173069b60c9c9";
        Log.d(TAG, "actionViews: test Id " + id);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebServices.API_GET_SENSORS + id, params, response -> {

            Log.d("Stats", "Stats: res " + response);

//            Sensors getSensors = new Sensors();
            ArrayList<Entry> entries = new ArrayList<>();


//            JSONArray jsonArray = new JSONArray();
//            try {
//                jsonArray = response.getJSONArray("data");
//
//                for (int i = 0; jsonArray.length() > i; i++){
//                    JSONObject jsonObject =jsonArray.getJSONObject(i);
//
//                }
//
//                Log.d("Stats", "actionViews: JSONArray is "+jsonArray.toString());
//
//            } catch (JSONException e) {
//                Log.d("Stats", "actionViews: JSONArray isnt found");
//
//                e.printStackTrace();
//
//            }
//
            JSONObject objnew = new JSONObject();

            try {
                JSONArray arr = response.getJSONArray("data");

                for (int i = 0; arr.length() > i; i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    if (obj.get("name").equals(SensorName)) {
                        JSONArray jsonArray = obj.getJSONArray("readings");

                        for (int j = 0; jsonArray.length() > j; j++) {
                            objnew = jsonArray.getJSONObject(j);


//                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
//                            format.setTimeZone(TimeZone.getTimeZone("UTC"));

//                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            String input = objnew.get("updatedAt").toString();

//                            DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            Date date = null;
                            try {
                                date = dateFormat.parse(input);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long unixTime = (long) date.getTime() / 1000;


                            Log.d(TAG, "actionViews: unix " + unixTime);


//                            Date date = null;
//                            String output = null;
//
//                            try{
//                                //Converting the input String to Date
//                                date= df.parse(input);
//                                //Changing the format of date and storing it in String
//                                output = outputformat.format(date);
//                                //Displaying the date
////                                System.out.println(output);
//                            }catch(ParseException pe){
//                                pe.printStackTrace();
//                            }

                            Log.d(TAG, "actionViews: output " + date);


//                            String date = objnew.get("updatedAt").toString();
//                            date = date.replaceAll("-", "");
//                            date = date.replaceAll(":", "");
//                            date = date.replaceAll("\\.", "");
//                            date = date.replaceAll("T", "");
//                            date = date.replaceAll("Z", "");

//                            Log.d(TAG, "actionViews: date is " + date);
//
//                            float d = Float.parseFloat(date) - 20200918171f;
//                            Log.d(TAG, "actionViews: float date " + d);
                            entries.add(new Entry((float) unixTime,
                                    Float.parseFloat(objnew.get("value").toString())));

//                            entries.add(new Entry((float) j,
//                                    Float.parseFloat(objnew.get("value").toString())));

//
//                            Log.d(TAG, "actionViews: testdata " + objnew.get("updatedAt"));
                        }
                    }
                }
            } catch (JSONException e) {
                Log.d(TAG, "actionViews: testdata ");
                e.printStackTrace();
            }

//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
//                    .create();
//            getSensors = gson.fromJson(response.toString(), Sensors.class);
//
//
//            Log.d("Stats", "actionViews: GOING IN TO LOOP ");
//            if (getSensors.data.get(0).name.equals(finalSensorName)) {
//                Log.d("Stats", "actionViews: GOING IN TO SECOND LOOP SIZE " + getSensors.data.get(0).readings.size());
//
//                for (int i = 0; getSensors.data.get(0).readings.size() > i; i++) {
//                    Log.d("Stats", "actionViews: INSIDE LAST LOOP AND VALUE OF I IS " + i);
//                    String date = getSensors.data.get(0).readings.get(i).updatedAt.toString();
//                    date = date.replaceAll("\\-", "");
//                    Log.d(TAG, "actionViews: THIS IS THE DATE " + date);
//                    Log.d("Stats", "actionViews: INSIDE THE LAST LOOP " + getSensors.data.get(0).readings.get(i).value);
//                    entries.add(new Entry((float) i, (float) getSensors.data.get(0).readings.get(i).value));
//                    Float.parseFloat(getSensors.data.get(0).readings.get(i).updatedAt.toString())
//                }
//            }


            LineDataSet lineDataSet = new LineDataSet(entries, SensorName);
            lineDataSet.setColor(Color.WHITE);
            lineDataSet.setValueTextSize(7);
            lineDataSet.setDrawValues(true);
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
//            xAxis.setLabelCount(5, true);
            xAxis.setCenterAxisLabels(true);

            binding.linechart.getXAxis().setValueFormatter(new MyAxisValueFormatter());

            binding.linechart.getAxisLeft().setTextColor(Color.WHITE);
            binding.linechart.getAxisRight().setTextColor(Color.WHITE);
            binding.linechart.getXAxis().setTextColor(Color.WHITE);

            binding.linechart.setScaleEnabled(true);
            binding.linechart.setPinchZoom(true);

            binding.progress.setVisibility(View.GONE);
            binding.linechart.setData(data);
//            binding.linechart.setVisibleXRangeMaximum(100);
            binding.linechart.invalidate();
            binding.linechart.setVisibility(View.VISIBLE);

        }, error -> {
            binding.progress.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getContext(), "Could not get Readings", Toast.LENGTH_SHORT);
            toast.show();
            Log.d("Stats", "_apiGetSensors: error " + error);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap<>();
                header.put("Authorization", sharedPrefs.getKey());
                Log.d("TAG", "getHeaders: " + header.toString());
                return header;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


    }

    private class MyAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {


//            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss a",Locale.ENGLISH);


            Date date = new Date((long) (value * 1000L));
            // format of the date
            SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            String java_date = jdf.format(date);
            Log.d(TAG, "getAxisLabel: unix " + java_date);

            return java_date;
        }
    }

}


