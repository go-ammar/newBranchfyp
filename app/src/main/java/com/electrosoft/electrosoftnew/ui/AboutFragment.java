package com.electrosoft.electrosoftnew.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.electrosoft.electrosoftnew.R;


public class AboutFragment extends Fragment {


    String URLr="http://electrosofttechnologies.com/apis/readNrooms.php";
    String URLs="http://www.electrosofttechnologies.com/apis/readNsensors.php";
    String URLas="http://www.electrosofttechnologies.com/apis/readNActivesensors.php";
    String URLds="http://www.electrosofttechnologies.com/apis/readNDeactivesensors.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }



    private void totalrooms()
    {
        StringRequest stringRequest = new StringRequest( Request.Method.GET, URLr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                String room = response;
                //roomno.setText(room);

                Toast.makeText(requireContext(), room, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(requireContext(), error+"not access", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

    }


    private void totalSensor()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                String sensor = response;
                //sensorno.setText(sensor);

                Toast.makeText(requireContext(), sensor, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(requireContext(), error+"not access", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

    }


    private void ActiveSensor()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                String AS = response;
                //asensor.setText(AS);

                Toast.makeText(requireContext(), AS, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(requireContext(), error+"not access", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

    }


    private void DeactiveSensor()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLds, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                String DS = response;
                //dsensor.setText(DS);

                Toast.makeText(requireContext(), DS, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(requireContext(), error+"not access", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

    }
}