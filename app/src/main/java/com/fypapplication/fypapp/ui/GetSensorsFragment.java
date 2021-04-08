package com.fypapplication.fypapp.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.SensorAdapter;
import com.fypapplication.fypapp.databinding.FragmentGetSensorsBinding;
import com.fypapplication.fypapp.models.Sensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetSensorsFragment extends Fragment {


    private static final String TAG = "GetSensorsFragment";
    private String URL;
    private SensorAdapter sensorAdapter;
    private RecyclerView recycle;
    private List<Sensor> lst_sens;
    NavController navController;
    FragmentGetSensorsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_sensors, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        recycle = binding.sensorlist;
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(requireContext()));
        lst_sens = new ArrayList<>();
        actionViews();
    }

    private void actionViews() {
        getsensor();
    }

    private void getsensor() {

        Log.d(TAG, "getsensor: ");
        String url = "http://localhost:8080/system/services/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Log.d("Response", response);
                        Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show();

                        if (response.equals("[]")) {

                            Toast.makeText(requireContext(), " access wali", Toast.LENGTH_SHORT).show();
                            //a.setVisibility(View.VISIBLE);
                            //neww();
                            //builder.setTitle("sorry");
                            //displayAlert("there is no data");
                        } else {
                            //a.setVisibility(View.INVISIBLE);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    Sensor s = new Sensor();
//                                    s.id = jsonObject.getInt("ID");
//                                    s.name = jsonObject.getString("name");
//                                    s.maxValue = jsonObject.getInt("avg");
//                                    lst_sens.add(s);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            sensorAdapter = new SensorAdapter(requireContext(), lst_sens);
                            recycle.setAdapter(sensorAdapter);
                        }

                    }
                }, error -> Toast.makeText(requireContext(), error + "not access", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                // params.put("roomno",String.valueOf(getid));



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

    }

}
