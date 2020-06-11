package com.electrosoft.electrosoftnew.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrosoft.electrosoftnew.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ContactFragment extends Fragment {

    private GoogleMap mMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    //@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ICCBS = new LatLng(24.9418, 67.1207);
        mMap.addMarker(new MarkerOptions().position(ICCBS).title("karachi univercity"));
        mMap.moveCamera( CameraUpdateFactory.newLatLng(ICCBS));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ICCBS,15));
        //mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
        //  @Override
        //public void onMapClick(LatLng latLng) {
        //  MarkerOptions markerOptions = new MarkerOptions();
        //markerOptions.position(latLng);
        //markerOptions.title(latLng.latitude+ ":" + latLng.longitude);
        //mMap.clear();
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        //mMap.addMarker(markerOptions);
        //}
        //});

    }
}